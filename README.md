[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.tomczyn.ellipse/ellipse-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.tomczyn.ellipse/ellipse-core)
## Ellipse

### Pragmatic Unidirectional Data Flow for Android

Ellipse is a library that helps to implement unidirectional data flow
in [Kotlin](https://github.com/jetbrains/kotlin)
using [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) in the most simplistic manner
possible. All API's are based on extension functions. Thanks to this design choice library plays
well with [Jetpack Compose](https://developer.android.com/jetpack/compose)
or [Dagger/Dagger Hilt](https://dagger.dev/).

### Adding dependency

```kotlin
dependencies {
    implementation("com.tomczyn.ellipse:ellipse-core:1.0.0")
    testImplementation("com.tomczyn.ellipse:ellipse-test:1.0.0")
}
```

### Glossary

- **Ellipse** - object that creates the unidirectional data flow loop.
- **Event** - Event produced by the view and consumed by the ellipse. E.g. button click.
- **State** - View's state.
- **Effect** - Events sent from the ellipse to the view, effects aren't cached for new
  subscribers, e.g. effects won't be resend during configuration change. They're useful for
  navigation, or showing popups and messages on the UI. E.g. `GoToHomeScreenEffect`.
- **PartialState** - Object to help modify the view state through `reduce` method.

View creates view events, which are sent to the ellipse. Ellipse maps view events to partial
states. Partial state modifies the view's state, which is sent back to view to be rendered.

Ellipse have minimal public API that is needed to create unidirectional data flow:

```kotlin
interface Ellipse<in EV : Any, out ST : Any, out EF : Any> {
    val state: StateFlow<ST>
    val effect: Flow<EF>
    fun sendEvent(event: EV)
}
```

### How to use it

1. Create ellipse object with one of the extension functions on `ViewModel` or `CoroutineScope`:

- `ellipse(...)`
    - It's good to define `typealias` for ellipse. So you won't have to write the generic types if you want to send it as an argument (for example to a Composable function).

```kotlin
typelias LoginEllipse = Ellipse<LoginEvent, LoginState, LoginEffect>

val ellipse: LoginEllipse = ellipse(
  initialState = LoginState(),
  prepare = { flowOf(/* ... */) },
  onEvent = { flowOf(/* ... */) }
)
```

- If you don't need effects, states or events you can put `Unit` as generic definition. For state you won't have to supply `initialState` and you won't have to return `Flow<PartialState<...>>` from `prepare` and `onEvent`. Example:

```kotlin
val ellipse: Ellipse<Unit, Unit, Unit> = ellipse(
    prepare = { /* Returns Unit instead of Flow */ },
    onEvent = { /* Returns Unit instead of Flow */ }
)
```

2. Subscribe to ellipse in view layer (Activity, Fragment):

- `onEllipse(lifecycleState = Lifecycle.State.###, ...)`

3. Or in Composable:

- `viewModel.ellipse.collectAsState { ... }`

#### Real world example - Login screen

First create state, effects, events and partial state classes. Then create ellipse in
the `ViewModel`.

```kotlin
data class LoginState(val isLoading: Boolean = false)

sealed interface LoginEvent {
    data class LoginClick(val email: String, val pass: String) : LoginEvent
}

sealed interface LoginEffect {
    object GoToHome : LoginEffect
    object ShowError : LoginEffect
}

sealed interface LoginPartialState : PartialState<LoginState> {
    object ShowLoading : LoginPartialState {
        override fun reduce(oldState: LoginState): LoginState = oldState.copy(isLoading = true)
    }

    object HideLoading : LoginPartialState {
        override fun reduce(oldState: LoginState): LoginState = oldState.copy(isLoading = false)
    }
}

typelias LoginEllipse = Ellipse<LoginEvent, LoginState, LoginEffect>

class LoginViewModel : ViewModel() {

    val ellipse: LoginEllipse = ellipse(LoginState()) { event ->
            when (event) {
                is LoginEvent.LoginClick -> flow {
                    emit(LoginPartialState.ShowLoading)
                    val isSuccess = loginUser(event.email, event.pass)
                    emit(LoginPartialState.HideLoading)
                    if (isSuccess) effects.send(LoginEffect.GoToHome)
                    else effects.send(LoginEffect.ShowError)
                }
            }
        }

    private suspend fun loginUser(email: String, pass: String): Boolean = TODO()
}
```

Then you can use it from view's layer

```kotlin
// Compose
@Composable
private fun EmailField() {
    val ellipse = viewModel<RegisterViewModel>().ellipse
    val email by ellipse.collectAsState { it.email }
    TextField(
        value = email,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { ellipse.sendEvent(RegisterEvent.EmailChanged(it)) },
        label = { Text(text = "Email") })
}

// Classic XML
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onEllipse(
            lifecycleState = Lifecycle.State.STARTED,
            ellipse = viewModel::ellipse,
            viewEvents = ::viewEvents,
            onState = ::render,
            onEffect = ::trigger
        )
    }

    private fun render(state: LoginState) = with(state) {
        binding.progress.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun viewEvents(): List<Flow<LoginEvent>> = listOf(
        binding.loginButton.clicks()
            .map { LoginEvent.LoginClick(binding.email.text, binding.pass.text) }
    )

    private fun trigger(effect: LoginEffect): Unit = when (effect) {
        LoginEffect.GoToHome -> openHome()
        LoginEffect.ShowError -> showErrorToast()
    }

    private fun openHome() = TODO()
    private fun showErrorToast() = TODO()
}
```
