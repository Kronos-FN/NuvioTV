import androidx.compose.desktop.Window
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize

fun main() = Window(title = "NuvioTV", size = IntSize(800, 600)) {
    // Entry point for NuvioTV Windows application
    Text("Welcome to NuvioTV")
}

@Preview
fun PreviewApp() {
    Text("Welcome to NuvioTV")
}