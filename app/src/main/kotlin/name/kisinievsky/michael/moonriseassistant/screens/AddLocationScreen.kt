package name.kisinievsky.michael.moonriseassistant.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class LocationInputMode { CITY, COORDINATES }

enum class AddLocationContext { FIRST_TIME, ADDITIONAL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationScreen(
    context: AddLocationContext = AddLocationContext.ADDITIONAL,
    inputMode: LocationInputMode = LocationInputMode.CITY,
    cityValue: String = "",
    latitudeValue: String = "",
    longitudeValue: String = "",
    nameValue: String = "",
    errorMessage: String? = null,
    isLoading: Boolean = false,
    onInputModeChange: (LocationInputMode) -> Unit = {},
    onCityChange: (String) -> Unit = {},
    onLatitudeChange: (String) -> Unit = {},
    onLongitudeChange: (String) -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onSave: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            when (context) {
                AddLocationContext.FIRST_TIME -> CenterAlignedTopAppBar(
                    title = { Text("Set Up Location") },
                )
                AddLocationContext.ADDITIONAL -> TopAppBar(
                    title = { Text("Add Location") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // First-time welcome art
            if (context == AddLocationContext.FIRST_TIME) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "\u263D",
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Where will you be watching\nthe moonrise?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(24.dp))
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tab selector
            InputModeSelector(
                selected = inputMode,
                onSelect = onInputModeChange,
                enabled = !isLoading,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Input fields
            when (inputMode) {
                LocationInputMode.CITY -> {
                    OutlinedTextField(
                        value = cityValue,
                        onValueChange = onCityChange,
                        label = { Text("City, State") },
                        placeholder = { Text("Seattle, WA") },
                        isError = errorMessage != null,
                        readOnly = isLoading,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    if (errorMessage != null) {
                        Text(
                            text = "\u26A0 $errorMessage",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                        )
                    }
                }
                LocationInputMode.COORDINATES -> {
                    OutlinedTextField(
                        value = latitudeValue,
                        onValueChange = onLatitudeChange,
                        label = { Text("Latitude") },
                        placeholder = { Text("47.6062") },
                        isError = errorMessage != null && errorMessage.contains("Latitude", ignoreCase = true),
                        readOnly = isLoading,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = longitudeValue,
                        onValueChange = onLongitudeChange,
                        label = { Text("Longitude") },
                        placeholder = { Text("-122.3321") },
                        isError = errorMessage != null && errorMessage.contains("Longitude", ignoreCase = true),
                        readOnly = isLoading,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    if (errorMessage != null) {
                        Text(
                            text = "\u26A0 $errorMessage",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Custom name field
            OutlinedTextField(
                value = nameValue,
                onValueChange = onNameChange,
                label = { Text("Location name (optional)") },
                placeholder = { Text("Home") },
                readOnly = isLoading,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action button
            when (context) {
                AddLocationContext.FIRST_TIME -> FilledTonalButton(
                    onClick = onSave,
                    enabled = !isLoading && errorMessage == null,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                        )
                        Text("  Verifying...", style = MaterialTheme.typography.labelLarge)
                    } else {
                        Text("Get Started")
                    }
                }
                AddLocationContext.ADDITIONAL -> Button(
                    onClick = onSave,
                    enabled = !isLoading && errorMessage == null,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        Text("  Verifying...", style = MaterialTheme.typography.labelLarge)
                    } else {
                        Text("Save")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputModeSelector(
    selected: LocationInputMode,
    onSelect: (LocationInputMode) -> Unit,
    enabled: Boolean = true,
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            selected = selected == LocationInputMode.CITY,
            onClick = { onSelect(LocationInputMode.CITY) },
            enabled = enabled,
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
        ) {
            Text("City Name")
        }
        SegmentedButton(
            selected = selected == LocationInputMode.COORDINATES,
            onClick = { onSelect(LocationInputMode.COORDINATES) },
            enabled = enabled,
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
        ) {
            Text("Coordinates")
        }
    }
}
