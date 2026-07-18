package jp.maple.exammap.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import jp.maple.exammap.model.ExamDatePrecision

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamDatePrecisionField(
    selectedPrecision: ExamDatePrecision,
    onPrecisionSelected: (ExamDatePrecision) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedPrecision.displayName,
            onValueChange = {},
            readOnly = true,
            label = {
                Text("日程精度")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            ExamDatePrecision.entries.forEach { precision ->
                DropdownMenuItem(
                    text = {
                        Text(precision.displayName)
                    },
                    onClick = {
                        onPrecisionSelected(precision)
                        expanded = false
                    }
                )
            }
        }
    }
}