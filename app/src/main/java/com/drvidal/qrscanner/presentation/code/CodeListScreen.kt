package com.drvidal.qrscanner.presentation.code

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.drvidal.qrscanner.R
import com.drvidal.qrscanner.presentation.WebViewActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import androidx.compose.material3.IconButton
import com.drvidal.qrscanner.presentation.settings.SettingsActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun CodeListScreen(viewModel: CodeListViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val scope = rememberCoroutineScope()

    val barcodeLauncher = rememberLauncherForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        result.contents?.let {
            viewModel.handleScannedCode(it)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ScanCode -> {
                    barcodeLauncher.launch(event.options)
                }
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.code.title + " " + context.getString(R.string.deleted),
                        actionLabel = context.getString(R.string.undo)
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.restoreLastDeletedCode()
                    }
                }
                is UiEvent.LaunchWebView -> {
                    WebViewActivity.openWebView(
                        context,
                        event.code.content,
                        event.code.title
                    )
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            CodeBottomSheetAction(viewModel) {
                scope.launch {
                    modalBottomSheetState.hide()
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text(stringResource(id = R.string.my_scans)) },
                    actions = {
                        IconButton(onClick = {
                            context.startActivity(Intent(context, SettingsActivity::class.java))
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "settings"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    icon = {
                        Icon(
                            painterResource(
                                id = R.drawable.ic_baseline_qr_code_scanner_24
                            ),
                            contentDescription = "Scan"
                        )
                    },
                    text = { Text(text = context.getString(R.string.scan)) },
                    onClick = {
                        viewModel.onScanClick()
                    })
            },

            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },

            content = {
                if (viewModel.codes.value.isEmpty() && !viewModel.isLoading.value) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_qr_code_scanner_24),
                            contentDescription = "code",
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(
                            text = stringResource(id = R.string.no_scans),
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp)
                    ) {
                        items(viewModel.codes.value) { code ->
                            CodeListItem(
                                code = code,
                                onItemClick = {
                                    viewModel.onCodeClick(code)
                                },
                                onMoreClick = {
                                    viewModel.onMoreClick(code)
                                    scope.launch {
                                        modalBottomSheetState.show()
                                    }
                                }
                            )
                            Divider()
                        }
                    }
                }
                if (viewModel.codeDetailDialog.value) {
                    CodeDetailDialog(viewModel)
                }
                if (viewModel.codeRenameDialog.value) {
                    CodeRenameDialog(viewModel)
                }
            }
        )
    }
}


