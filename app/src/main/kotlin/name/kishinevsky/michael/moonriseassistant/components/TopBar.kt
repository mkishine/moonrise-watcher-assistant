package name.kishinevsky.michael.moonriseassistant.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    locationName: String,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    onRefreshClick: (() -> Unit)? = null,
    onLocationNameClick: (() -> Unit)? = null,
) {
    TopAppBar(
        title = {
            if (onLocationNameClick != null) {
                TextButton(
                    onClick = onLocationNameClick,
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                ) {
                    Text(locationName, style = MaterialTheme.typography.titleLarge)
                }
            } else {
                Text(locationName)
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                )
            }
        },
        actions = {
            if (onRefreshClick != null) {
                IconButton(onClick = onRefreshClick) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                    )
                }
            }
        },
        modifier = modifier,
    )
}
