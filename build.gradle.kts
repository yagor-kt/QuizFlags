// Root build-скрипт проекта QuizFlags.
// Здесь только объявляем плагины, само применение — в модуле app.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}