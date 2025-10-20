const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.enviarNotificacion = functions.https.onCall(async (data, context) => {
  const usuarioId = data.usuarioId;
  const titulo = data.titulo;
  const mensaje = data.mensaje;
  const tipo = data.tipo;

  try {
    const userDoc = await admin.firestore()
        .collection("usuarios")
        .doc(usuarioId)
        .get();

    if (!userDoc.exists) {
      console.log("Usuario no encontrado:", usuarioId);
      return {success: false, message: "Usuario no encontrado"};
    }

    const token = userDoc.data().fcmToken;

    if (!token) {
      console.log("No hay token para el usuario:", usuarioId);
      return {success: false, message: "Token no encontrado"};
    }

    const message = {
      notification: {
        title: titulo,
        body: mensaje,
      },
      data: {
        tipo: tipo,
      },
      token: token,
    };

    const response = await admin.messaging().send(message);
    console.log("Notificación enviada:", response);
    return {success: true, messageId: response};
  } catch (error) {
    console.error("Error:", error);
    return {success: false, error: error.message};
  }
});
