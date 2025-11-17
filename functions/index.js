const admin = require("firebase-admin");
const {initializeApp} = require("firebase-admin/app");
const {onCall, HttpsError} = require("firebase-functions/v2/https");
const {onDocumentCreated} = require("firebase-functions/v2/firestore");
const {getMessaging} = require("firebase-admin/messaging");
const {onSchedule} = require("firebase-functions/v2/scheduler");

initializeApp();

// Función para ser llamada desde la app (ej. bienvenida, confirmación)
exports.enviarNotificacion = onCall(async (request) => {
  const {usuarioId, titulo, mensaje, tipo} = request.data;

  console.log(`Intentando enviar notificación para el usuario: ${usuarioId}`);

  if (!usuarioId || typeof usuarioId !== "string") {
    console.error("ID de usuario inválido o faltante.", request.data);
    throw new HttpsError(
        "invalid-argument",
        "La función debe ser llamada con un 'usuarioId' válido.",
    );
  }

  try {
    const userDoc = await admin.firestore().collection("usuarios").doc(usuarioId).get();
    if (!userDoc.exists) {
      console.log("Usuario no encontrado:", usuarioId);
      throw new HttpsError("not-found", `Usuario ${usuarioId} no encontrado.`);
    }

    const token = userDoc.data().fcmToken;
    if (!token) {
      console.log("No hay token FCM para el usuario:", usuarioId);
      return {success: false, error: "Token no encontrado"};
    }

    const payload = {
      data: {
        title: titulo,
        body: mensaje,
        tipo: tipo || "general",
        usuarioId: usuarioId,
      },
      token: token,
      android: {
        priority: "high",
        notification: {
          title: titulo,
          body: mensaje,
          channelId: "general_channel",
        },
      },
    };

    const response = await getMessaging().send(payload);
    console.log("Notificación enviada con éxito:", response);
    return {success: true, messageId: response};
  } catch (error) {
    console.error("Error procesando la notificación:", error);
    if (error instanceof HttpsError) throw error;
    throw new HttpsError("unknown", "Ocurrió un error desconocido.", error.message);
  }
});

// Función que se activa al crear una nueva cita en Firestore para notificar al TERAPEUTA
exports.notificarTerapeutaNuevaCita = onDocumentCreated("citas/{citaId}", async (event) => {
  const snap = event.data;
  if (!snap) {
    console.log("No data associated with the event");
    return;
  }
  const citaData = snap.data();
  const {servicio, fecha} = citaData;

  if (!servicio) {
    console.log("La cita no tiene un 'servicio'. No se puede encontrar al terapeuta.");
    return null;
  }

  console.log(`Nueva cita para ${servicio}. Buscando terapeuta.`);

  try {
    const servicioDoc = await admin.firestore().collection("servicios").doc(servicio).get();
    if (!servicioDoc.exists) {
      console.error("No se encontró el servicio:", servicio);
      return null;
    }
    const nombreServicio = servicioDoc.data().servicio;

    const terapeutasSnapshot = await admin.firestore().collection("usuarios")
        .where("rol", "==", "terapeuta")
        .where("terapeuta_servicio", "==", servicio)
        .limit(1).get();

    if (terapeutasSnapshot.empty) {
      console.error("No se encontró un terapeuta para el servicio:", servicio);
      return null;
    }

    const terapeutaDoc = terapeutasSnapshot.docs[0];
    const terapeutaId = terapeutaDoc.id;
    const token = terapeutaDoc.data().fcmToken;

    if (!token) {
      console.error("No se encontró token FCM para el terapeuta:", terapeutaId);
      return null;
    }

    const title = "¡Nueva Cita Asignada!";
    const body = `Tienes una nueva cita para un servicio de ${nombreServicio} el día ${fecha}.`;
    const payload = {
      data: {
        title: title,
        body: body,
        tipo: "nueva_cita",
        usuarioId: terapeutaId,
      },
      token: token,
      android: {
        priority: "high",
        notification: {
          title: title,
          body: body,
          channelId: "citas_terapeuta_channel",
        },
      },
    };

    const response = await getMessaging().send(payload);
    console.log("Notificación de nueva cita enviada con éxito al terapeuta:", response);
    return response;
  } catch (error) {
    console.error("Error al enviar la notificación al terapeuta:", error);
    return null;
  }
});

// Función que se activa al crear una nueva cita en Firestore para notificar al CLIENTE
exports.notificarClienteNuevaCita = onDocumentCreated("citas/{citaId}", async (event) => {
  const snap = event.data;
  if (!snap) {
    console.log("No data associated with the event");
    return;
  }
  const citaData = snap.data();
  const {cliente_id, servicio, fecha} = citaData;

  if (!cliente_id) {
    console.log("La cita no tiene un 'cliente_id'. No se puede notificar.");
    return null;
  }

  console.log(`Nueva cita creada por ${cliente_id} para el servicio ${servicio}.`);

  try {
    const clienteDoc = await admin.firestore().collection("usuarios").doc(cliente_id).get();
    if (!clienteDoc.exists) {
      console.error("No se encontró el cliente:", cliente_id);
      return null;
    }
    const token = clienteDoc.data().fcmToken;

    if (!token) {
      console.error("No se encontró token FCM para el cliente:", cliente_id);
      return null;
    }

    const servicioDoc = await admin.firestore().collection("servicios").doc(servicio).get();
    if (!servicioDoc.exists) {
      console.error("No se encontró el servicio:", servicio);
      return null;
    }
    const nombreServicio = servicioDoc.data().servicio;
    const title = "¡Cita Confirmada!";
    const body = `Tu cita para ${nombreServicio} el ${fecha} ha sido agendada con éxito.`;
    const payload = {
      data: {
        title: title,
        body: body,
        tipo: "nueva_cita_cliente",
        citaId: event.params.citaId,
        usuarioId: cliente_id, // Asegura que se guarda para el cliente correcto
      },
      token: token,
      android: {
        priority: "high",
        notification: {
          title: title,
          body: body,
          channelId: "citas_channel", // ¡IMPORTANTE! Este canal debe existir en tu app Android.
        },
      },
    };

    const response = await getMessaging().send(payload);
    console.log("Notificación de nueva cita enviada con éxito al cliente:", response);
    return response;
  } catch (error) {
    console.error("Error al enviar la notificación al cliente:", error);
    return null;
  }
});

// Función programada para enviar recordatorios 24 horas antes de la cita.
exports.enviarRecordatoriosDeCitas = onSchedule("every 1 hours", async (event) => {
    console.log("Ejecutando la función de recordatorios de citas.");
    const ahora = admin.firestore.Timestamp.now();

    const inicioRango = new admin.firestore.Timestamp(ahora.seconds + (24 * 60 * 60), ahora.nanoseconds);
    const finRango = new admin.firestore.Timestamp(ahora.seconds + (25 * 60 * 60), ahora.nanoseconds);

    try {
        const querySnapshot = await admin.firestore().collection('citas')
            .where('fecha', '>=', inicioRango)
            .where('fecha', '<', finRango)
            .where('recordatorioEnviado', '!=', true)
            .get();

        if (querySnapshot.empty) {
            console.log("No hay citas para recordar en la próxima hora.");
            return null;
        }

        console.log(`Se encontraron ${querySnapshot.docs.length} citas para recordar.`);

        const promises = querySnapshot.docs.map(async (doc) => {
            const cita = doc.data();
            const citaId = doc.id;
            const { cliente_id, servicio, fecha } = cita;

            if (!cliente_id) {
                console.log(`La cita ${citaId} no tiene cliente_id. Saltando.`);
                return;
            }

            try {
                const [clienteDoc, servicioDoc] = await Promise.all([
                    admin.firestore().collection("usuarios").doc(cliente_id).get(),
                    admin.firestore().collection("servicios").doc(servicio).get()
                ]);

                if (!clienteDoc.exists || !servicioDoc.exists) {
                    console.error(`Cliente o servicio no encontrado para la cita ${citaId}.`);
                    return;
                }

                const token = clienteDoc.data().fcmToken;
                const nombreServicio = servicioDoc.data().servicio;
                const fechaLegible = new Date(fecha.toMillis()).toLocaleString('es-ES', { dateStyle: 'full', timeStyle: 'short' });

                const title = "Recordatorio de Cita";
                const body = `Te recordamos tu cita para ${nombreServicio} el ${fechaLegible}. ¡No faltes!`;

                const notificacion = {
                    titulo: title,
                    mensaje: body,
                    fecha: admin.firestore.FieldValue.serverTimestamp(),
                    leida: false,
                    tipo: "recordatorio_cita",
                    citaId: citaId,
                };
                await admin.firestore().collection("usuarios").doc(cliente_id).collection("notificaciones").add(notificacion);

                if (token) {
                    const payload = {
                        data: {
                            title: title,
                            body: body,
                            tipo: "recordatorio_cita",
                            citaId: citaId,
                            usuarioId: cliente_id,
                        },
                        token: token,
                        android: {
                            priority: "high",
                            notification: {
                                title: title,
                                body: body,
                                channelId: "recordatorios_channel",
                            },
                        },
                    };
                    await getMessaging().send(payload);
                    console.log(`Notificación de recordatorio enviada para la cita ${citaId}`);
                } else {
                    console.log(`No se encontró token FCM para ${cliente_id}. La notificación solo fue guardada en su historial.`);
                }

                return doc.ref.update({ recordatorioEnviado: true });

            } catch (error) {
                console.error(`Error procesando el recordatorio para la cita ${citaId}:`, error);
            }
        });

        await Promise.all(promises);
        console.log("Procesamiento de recordatorios completado.");
        return null;

    } catch (error) {
        console.error("Error en la función programada de recordatorios:", error);
        return null;
    }
});
