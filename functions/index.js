const admin = require("firebase-admin");
const {initializeApp} = require("firebase-admin/app");
const {onCall, HttpsError} = require("firebase-functions/v2/https");
const {onDocumentCreated} = require("firebase-functions/v2/firestore");
const {getMessaging} = require("firebase-admin/messaging");

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
    // Terapeutas y clientes están en la colección 'usuarios'
    const userDoc = await admin.firestore()
        .collection("usuarios").doc(usuarioId).get();

    if (!userDoc.exists) {
      console.log("Usuario no encontrado:", usuarioId);
      throw new HttpsError(
          "not-found", `Usuario ${usuarioId} no encontrado.`,
      );
    }

    const token = userDoc.data().fcmToken;

    if (!token) {
      console.log("No hay token FCM para el usuario:", usuarioId);
      // No es un error fatal
      return {success: false, error: "Token no encontrado"};
    }

    const message = {
      notification: {
        title: titulo,
        body: mensaje,
      },
      data: {
        tipo: tipo || "general",
      },
      token: token,
    };

    const response = await getMessaging().send(message);
    console.log("Notificación enviada con éxito:", response);
    return {success: true, messageId: response};
  } catch (error) {
    console.error("Error procesando la notificación:", error);
    if (error instanceof HttpsError) {
      throw error;
    }
    throw new HttpsError(
        "unknown",
        "Ocurrió un error desconocido.",
        error.message,
    );
  }
});

// Función que se activa al crear una nueva cita en Firestore
exports.notificarTerapeutaNuevaCita = onDocumentCreated("citas/{citaId}", async (event) => {
  const snap = event.data;
  if (!snap) {
    console.log("No data associated with the event");
    return;
  }
  const citaData = snap.data();

  const {servicio, fecha} = citaData;

  if (!servicio) {
    console.log(
        "La cita no tiene un 'servicio'. No se puede encontrar al terapeuta.",
    );
    return null;
  }

  console.log(
      `Nueva cita creada para el servicio ${servicio}. Buscando terapeuta. `,
  );

  try {
    // Obtener el nombre del servicio
    const servicioDoc = await admin.firestore().collection("servicios").doc(servicio).get();
    if (!servicioDoc.exists) {
      console.error("No se encontró el servicio:", servicio);
      return null;
    }
    const nombreServicio = servicioDoc.data().servicio;

    // Find the therapist assigned to the service
    const terapeutasSnapshot = await admin.firestore()
        .collection("usuarios")
        .where("rol", "==", "terapeuta")
        .where("terapeuta_servicio", "==", servicio)
        .limit(1)
        .get();

    if (terapeutasSnapshot.empty) {
      console.error("No se encontró un terapeuta para el servicio:", servicio);
      return null;
    }

    const terapeutaDoc = terapeutasSnapshot.docs[0];
    const terapeutaId = terapeutaDoc.id;
    const token = terapeutaDoc.data().fcmToken;

    if (!token) {
      console.error(
          "No se encontró token FCM para el terapeuta:",
          terapeutaId,
      );
      return null;
    }

    // Mensaje de la notificación
    const body =
      `Tienes una nueva cita para un servicio de ${nombreServicio} ` +
      `el día ${fecha}.`;

    const payload = {
      notification: {
        title: "¡Nueva Cita Asignada!",
        body: body,
      },
      data: {
        tipo: "nueva_cita",
      },
      token: token,
    };

    // Enviar la notificación
    const response = await getMessaging().send(payload);
    console.log("Notificación de nueva cita enviada con éxito:", response);
    return response;
  } catch (error) {
    console.error("Error al enviar la notificación al terapeuta:", error);
    return null;
  }
});
