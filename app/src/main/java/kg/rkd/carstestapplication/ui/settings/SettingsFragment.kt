package kg.rkd.carstestapplication.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import kg.rkd.carstestapplication.R
import kg.rkd.carstestapplication.ui.CarsViewModel
import kg.rkd.carstestapplication.ui_components.DefaultAppBar
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SettingsFragment : Fragment() {

    private val viewModel: CarsViewModel by lazy {
        requireParentFragment().getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SettingsScreen(
                    isSubscribed = viewModel.isSubscribed()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    isSubscribed: State<Boolean>,
    triesCount: Int,
    onBackPressed: () -> Unit = {},
    onRestoreClick: () -> Unit = {},
) {
    Scaffold(topBar = {
        DefaultAppBar(onBackPressed = onBackPressed)
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(6.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val screenHeight = LocalConfiguration.current.screenHeightDp
            val screenWidth = LocalConfiguration.current.screenWidthDp
            val isPortrait =
                LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT


            SubscriptionStatusComponent(
                modifier = Modifier.fillMaxWidth(),
                isSubscribed = isSubscribed.value
            )

            val dividerWidth =
                if (isPortrait) (screenWidth - screenWidth / 4).dp else (screenWidth / 2).dp
            Divider(
                modifier = Modifier
                    .widthIn(max = dividerWidth)
                    .padding(9.dp)
            )

            CarsCanBeAddedComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp), value = triesCount
            )


            RestoreAppStateComponent(
                modifier = Modifier
                    .widthIn(min = (screenWidth / 2).dp, max = screenWidth.dp)
                    .heightIn(min = (screenHeight / 3).dp, max = screenHeight.dp),
                onClick = onRestoreClick
            )
        }
    }
}

@Composable
private fun SubscriptionStatusComponent(modifier: Modifier = Modifier, isSubscribed: Boolean) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(45.dp),
            painter = painterResource(id = R.drawable.ic_subscription),
            contentDescription = "subscription",
            tint = MaterialTheme.colorScheme.tertiary,
        )

        val text = buildAnnotatedString {
            append("Subscription ")
            withStyle(SpanStyle(if (isSubscribed) Color.Green else MaterialTheme.colorScheme.error)) {
                if (isSubscribed) {
                    append("is active")
                } else append("is not active")
            }
        }
        Text(
            modifier = Modifier.padding(6.dp),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun CarsCanBeAddedComponent(modifier: Modifier, value: Int) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        Text(text = "Cars can be added to collection: ", style = MaterialTheme.typography.bodyLarge)

        val countText = buildAnnotatedString {
            when (value) {
                0 -> {
                    withStyle(SpanStyle(MaterialTheme.colorScheme.error)) {
                        append(value.toString())
                    }
                }
                1 -> {
                    withStyle(SpanStyle(Color.Cyan)) {
                        append(value.toString())
                    }
                }
                else -> {
                    withStyle(SpanStyle(Color.Green)) {
                        append(value.toString())
                    }
                }
            }
        }
        Text(text = countText)
    }
}

@Composable
private fun RestoreAppStateComponent(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(modifier = modifier, onClick = onClick) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }

        Text(text = "Restore")
    }
}

@Composable
@Preview
private fun Preview() {
    SettingsScreen()
}