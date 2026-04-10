package com.example.appointmentschedulingapp.ui.features.patient.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        placeholder = {
            if (placeholder.isNotEmpty()) Text(placeholder)
        },
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun ProfileMultilineField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        minLines = 3,
        shape = RoundedCornerShape(10.dp)
    )
}