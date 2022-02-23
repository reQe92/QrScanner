package com.drvidal.qrscanner.presentation.code

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.drvidal.qrscanner.R

@ExperimentalMaterialApi
@Composable
fun CodeRenameDialog(viewModel: CodeListViewModel) {

    AlertDialog(
        onDismissRequest = {
            viewModel.onRenameDialogClose()
        },
        title = {
            Text(text = stringResource(id = R.string.rename))
        },
        text = {
            TextField(
                value = viewModel.newCodeName.value,
                onValueChange = {
                    viewModel.onRenameValueChange(it)
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.onRenameDialogClose()
                }
            ) {
                Text(stringResource(id = R.string.close))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.renameCode()
                    viewModel.onRenameDialogClose()
                }
            ) {
                Text(stringResource(id = R.string.rename))
            }
        }
    )
}