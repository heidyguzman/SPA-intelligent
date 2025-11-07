const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.enviarNotificacion = functions.https.onCall(async (data, context) => {
  // The actual payload is nested inside a 'data' property.
  const {usuarioId, titulo, mensaje, tipo} = data.data;

  console.log(`Attempting to send notification for user: ${usuarioId}`);

  if (!usuarioId || typeof usuarioId !== "string") {
    console.error("Invalid or missing usuarioId. Full object received:", data);
    throw new functions.https.HttpsError(
        "invalid-argument",
        "The function must be called with a valid 'usuarioId'.",
    );
  }

  try {
    const userDoc = await admin.firestore()
        .collection("usuarios")
        .doc(usuarioId)
        .get();

    if (!userDoc.exists) {
      console.log("Usuario no encontrado:", usuarioId);
      throw new functions.https.HttpsError(
          "not-found", `User ${usuarioId} not found.`);
    }

    const token = userDoc.data().fcmToken;

    if (!token) {
      console.log("No hay token para el usuario:", usuarioId);
      throw new functions.https.HttpsError(
          "failed-precondition",
          `FCM token not found for user ${usuarioId}.`,
      );
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
    console.error("Error processing notification:", error);
    if (error instanceof functions.https.HttpsError) {
      throw error;
    }
    throw new functions.https.HttpsError(
        "unknown",
        "An unknown error occurred.",
        error.message,
    );
  }
});
