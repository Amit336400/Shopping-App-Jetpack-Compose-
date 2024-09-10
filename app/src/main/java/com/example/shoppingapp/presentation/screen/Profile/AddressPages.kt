import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.shoppingapp.common.LoadingDialog
import com.example.shoppingapp.common.UserLocation
import com.example.shoppingapp.presentation.screen.order.orderViewModel
import androidx.compose.ui.focus.FocusDirection

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddressPopup(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    viewModel: orderViewModel = hiltViewModel(),
    name: String? = "",
    address: String? = "",
    pin: String? = "",
    state: String? = "",
    phoneNo: String? = "",
) {
    val data = viewModel.addLocation.collectAsStateWithLifecycle().value
    LoadingDialog(show = data.isLoading)

    val nameState = remember { mutableStateOf(name) }
    val addressState = remember { mutableStateOf(address) }
    val stateState = remember { mutableStateOf(state) }
    val pinState = remember { mutableStateOf(pin) }
    val phoneState = remember { mutableStateOf(phoneNo) }

    // Scroll state for the Column
    val scrollState = rememberScrollState()

    // To handle keyboard actions
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text("Enter Address") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState) // Enable scrolling
                        .imePadding() // Adds padding to prevent keyboard overlap
                        .padding(bottom = 16.dp) // Ensure extra padding for safety
                ) {
                    // Input fields for address
                    TextField(
                        value = nameState.value!!,
                        onValueChange = { nameState.value = it },
                        label = { Text("Name") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = addressState.value!!,
                        onValueChange = { addressState.value = it },
                        label = { Text("Address") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = stateState.value!!,
                        onValueChange = { stateState.value = it },
                        label = { Text("State") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = pinState.value!!,
                        onValueChange = { pinState.value = it },
                        label = { Text("Zip Code") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = phoneState.value!!,
                        onValueChange = { phoneState.value = it },
                        label = { Text("Phone Number") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addLocation(
                            userLocation = UserLocation(
                                pin = pinState.value!!,
                                name = nameState.value!!,
                                phoneNo = phoneState.value!!,
                                address = addressState.value!!,
                                state = stateState.value!!
                            )
                        )
                        onDismissRequest()
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismissRequest
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
