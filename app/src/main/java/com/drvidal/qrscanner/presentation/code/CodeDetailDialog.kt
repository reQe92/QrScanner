package com.drvidal.qrscanner.presentation.code

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.drvidal.qrscanner.R

@ExperimentalMaterialApi
@Composable
fun CodeDetailDialog(viewModel: CodeListViewModel) {
    AlertDialog(
        onDismissRequest = {
            viewModel.onDetailDialogClose()
        },
        title = {
            Text(text = viewModel.lastSelectedCode.value?.title ?: "")
        },
        text = {
            Text(text = viewModel.lastSelectedCode.value?.content ?: "")
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.onDetailDialogClose()
                }
            ) {
                Text(stringResource(id = R.string.close))
            }
        },
        confirmButton = {}
    )
}