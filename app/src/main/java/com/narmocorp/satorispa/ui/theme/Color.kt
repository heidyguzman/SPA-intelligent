package com.narmocorp.satorispa.ui.theme

import androidx.compose.ui.graphics.Color

// ==============================================================================
// PALETA DE COLORES DE SATORI SPA (SE USARÁN EN EL ARCHIVO Theme.kt)
// ==============================================================================

// Colores de Marca
val Satori_Primary = Color(0xFF995D2D)   // Marrón Oscuro Principal
val Satori_Secondary = Color(0xFFDBBBA6) // Marrón Claro/Beige Secundario
val Satori_Tertiary = Color(0xFFB08D73)   // Marrón para elementos de fondo (ej: selector)

// Colores de Texto y Superficie (Light Mode - Valores actuales)
val Satori_Text_Dark = Color(0xFF71390C) // Texto Café Oscuro (textOnSecondaryPlatform)
val Satori_Text_Light = Color.White       // Texto que va sobre el Primary
val Satori_Background_Light = Color.White // Fondo principal (páginas)
val Satori_Surface_Light = Color(0xFFF5F5F5) // Superficies claras/Cards de fondo (ej: Configuración)

// Colores de Botones y Elementos Específicos
val Satori_Error = Color(0xFFE53935)     // Rojo (para eliminar o errores de Notificaciones)
val Satori_Success = Color(0xFF4CAF50)   // Verde (para éxito o validaciones)
val Satori_Notification_Badge = Color(0xFF2196F3) // Azul (para badges)

// ==============================================================================
// ESQUEMA DE COLORES LIGHT (Claro)
// ==============================================================================

val LightPrimary = Satori_Primary
val LightOnPrimary = Satori_Text_Light
val LightSecondary = Satori_Secondary
val LightOnSecondary = Satori_Text_Dark // El texto oscuro que usas sobre las superficies claras
val LightTertiary = Satori_Tertiary
val LightBackground = Satori_Background_Light
val LightSurface = Satori_Surface_Light
val LightError = Satori_Error

// ==============================================================================
// ESQUEMA DE COLORES DARK (Oscuro)
// Ajustados para visibilidad en fondo oscuro. Usaremos el Primary/Secondary como fondo.
// ==============================================================================

val DarkPrimary = Satori_Primary
val DarkOnPrimary = Satori_Text_Light // Blanco sobre el color primario
val DarkSecondary = Color(0xFF333333) // << CORRECCIÓN: Fondo oscuro para Secondary en Dark Mode
val DarkOnSecondary = Color.White    // << CORRECCIÓN: Blanco para asegurar contraste en el Header
val DarkTertiary = Satori_Tertiary

// Para el modo oscuro, la superficie debe ser oscura y el texto claro
val DarkBackground = Color(0xFF1C1C1E) // Un gris oscuro para el fondo
val DarkSurface = Color(0xFF333333)    // Una superficie ligeramente más clara para Cards
val DarkOnBackground = Color.White     // Texto blanco en fondos oscuros
val DarkOnSurface = Color.White        // Texto blanco en superficies oscuras
val DarkError = Color(0xFFCF6679) // Rojo claro para errores en modo oscuro
