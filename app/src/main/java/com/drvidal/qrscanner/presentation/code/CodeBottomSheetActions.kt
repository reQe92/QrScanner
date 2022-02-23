package com.drvidal.qrscanner.presentation.code

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.drvidal.qrscanner.R
import com.drvidal.qrscanner.presentation.code.CodeListViewModel

@ExperimentalMaterialApi
@Composable
fun CodeBottomSheetAction(viewModel: CodeListViewModel, onClose: () -> Unit) {

    Column {
        Text(
            text = viewModel.lastSelectedCode.value?.title ?: "",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Divider()

        ListItem(
            text = { Text(stringResource(id = R.string.show)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_remove_red_eye_24),
                    contentDescription = "show"
                )
            },
            modifier = Modifier.clickable {
                val code = viewModel.lastSelectedCode.value
                if (code != null) {
                    viewModel.onCodeClick(code)
                }
                onClose()
            }
        )
        ListItem(
            text = { Text(stringResource(id = R.string.rename)) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_drive_file_rename_outline_24),
                    contentDescription = "rename"
                )
            },
            modifier = Modifier.clickable {
                viewModel.onRenameClick()
                onClose()
            }
        )
        ListItem(
            text = { Text(stringResource(id = R.string.delete)) },
            icon = {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "delete"
                )
            },
            modifier = Modifier.clickable {
                val code = viewModel.lastSelectedCode.value
                if (code != null) {
                    viewModel.deleteCode(code)
                }
                onClose()
            }
        )
    }
}

