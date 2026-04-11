package com.example.appointmentschedulingapp.ui.features.tickets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.ui.features.tickets.components.BookingCard
import com.example.appointmentschedulingapp.ui.features.tickets.components.StatusUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketsScreen(
    onViewDetail: (Booking) -> Unit = {},
    viewModel: TicketsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            val primaryBlue = Color(0xFF1976D2)

            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Phiếu khám",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "${uiState.bookings.size} lịch hẹn",
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // TODO back
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.loadBookings()
                    }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = primaryBlue
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            // ── Filter chips ─────────────────────────────────────────────────
            LazyRow(
                modifier = Modifier
                    .background(colorScheme.surface)
                    .padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(TicketFilter.entries) { filter ->
                    val selected = uiState.selectedFilter == filter
                    FilterChip(
                        selected = selected,
                        onClick = { viewModel.selectFilter(filter) },
                        label = {
                            Text(
                                filter.label,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colorScheme.primary,
                            selectedLabelColor = colorScheme.onPrimary,
                            containerColor = colorScheme.surfaceVariant,
                            labelColor = colorScheme.onSurfaceVariant
                        ),
                        border = null,
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            // ── Content ──────────────────────────────────────────────────────
            PullToRefreshBox(
                isRefreshing = uiState.isLoading,
                onRefresh = viewModel::loadBookings,
                modifier = Modifier.fillMaxSize()
            ) {
                AnimatedContent(
                    targetState = uiState,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tickets_content"
                ) { state ->
                    when {
                        state.isLoading && state.bookings.isEmpty() -> {
                            LoadingState()
                        }

                        state.errorMessage != null -> {
                            ErrorState(
                                message = state.errorMessage,
                                onRetry = viewModel::loadBookings
                            )
                        }

                        state.filteredBookings.isEmpty() -> {
                            EmptyState(filter = state.selectedFilter)
                        }

                        else -> {
                            LazyColumn(
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(
                                    items = state.filteredBookings,
                                    key = { it.id }
                                ) { booking ->
                                    BookingCard(
                                        booking = booking,
                                        onClick = {
                                            viewModel.selectBooking(booking)
                                            onViewDetail(booking)
                                        }
                                    )
                                }
                                item { Spacer(Modifier.height(80.dp)) }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Booking Card ──────────────────────────────────────────────────────────────



// ── Status Badge ──────────────────────────────────────────────────────────────




fun BookingStatus.toUi(): StatusUi = when (this) {
    BookingStatus.PENDING   -> StatusUi(label, Icons.Outlined.HourglassEmpty)
    BookingStatus.CONFIRMED -> StatusUi(label, Icons.Outlined.CheckCircle)
    BookingStatus.UNPAID    -> StatusUi(label, Icons.Outlined.Payment)
    BookingStatus.PAID      -> StatusUi(label, Icons.Outlined.TaskAlt)
    BookingStatus.COMPLETED -> StatusUi(label, Icons.Outlined.MedicalServices)
    BookingStatus.CANCELLED -> StatusUi(label, Icons.Outlined.Cancel)
}

// ── Atoms ─────────────────────────────────────────────────────────────────────

@Composable
fun InfoChipSmall(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = colorScheme.primary.copy(alpha = 0.8f),
            modifier = Modifier.size(12.dp)
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ── States ────────────────────────────────────────────────────────────────────

@Composable
 fun LoadingState() {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(4) {
            Card(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {}
        }
    }
}

@Composable
 fun ErrorState(message: String, onRetry: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.CloudOff,
            contentDescription = null,
            tint = colorScheme.outline,
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Có lỗi xảy ra",
            style = MaterialTheme.typography.titleMedium,
            color = colorScheme.onSurface
        )
        Text(
            message,
            style = MaterialTheme.typography.bodySmall,
            color = colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onRetry) {
            Icon(Icons.Default.Refresh, null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Thử lại")
        }
    }
}

@Composable
 fun EmptyState(filter: TicketFilter) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(colorScheme.primaryContainer.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.EventBusy,
                contentDescription = null,
                tint = colorScheme.primary.copy(alpha = 0.7f),
                modifier = Modifier.size(50.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            if (filter == TicketFilter.ALL)
                "Bạn chưa có phiếu khám nào"
            else
                "Không có phiếu \"${filter.label}\"",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Text(
            "Đặt lịch khám để bắt đầu",
            style = MaterialTheme.typography.bodySmall,
            color = colorScheme.onSurface.copy(alpha = 0.45f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

 fun formatPrice(price: Long): String {
    if (price <= 0L) return "Miễn phí"
    return "%,d đ".format(price).replace(',', '.')
}