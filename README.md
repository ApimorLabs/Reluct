# ⌛ Reluct

![](https://imgur.com/z4lc8oX)

<a href="https://twitter.com/rackadev" target="_blank">
    <img alt="Twitter: rackadev" src="https://img.shields.io/twitter/follow/rackadev.svg?style=social" />
    </a>

> An app to manage your screen time, assign Tasks and set personal goals. Works on Android with Desktop support in the works.

### ✨ Documentation

- [Coming Soon]()

### 🤳 Screenshots

![](https://imgur.com/whn46mn)
![](https://imgur.com/sqob1hb)
![](https://imgur.com/o0I6v5v)
![](https://imgur.com/T4ggdka)

## 💻 Install

| Platform          | Download                                                                                                                                                                       | Status          |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------|
| Android           | [![Download Button](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=work.racka.reluct) | 🧪 Alpha        |
| Desktop - Windows | [![Download Button](https://img.shields.io/static/v1?label=In-Progress&message=v0.0.0-experimental00&color=green)]()                                                           | 🧪 Not Released |
| Desktop - macOS   | [![Download Button](https://img.shields.io/static/v1?label=In-Progress&message=v0.0.0-experimental00&color=yellow)]()                                                          | 🧪 Not Released |
| Desktop - Linux   | [![Download Button](https://img.shields.io/static/v1?label=In-Progress&message=v0.0.0-experimental00&color=purple)]()                                                          | 🧪 Not Released |

> ℹ️ Compose Debug apks are sometimes laggy as they contain a lot of debug code.
>
> ℹ️ Download the app from Play Store and it will have the expected performance.

## 🏋 Requirements

- Java 11 or above
- Android Studio Dolphin | 2021.3+

## 🏗️️ Built with

| Component         | Libraries                                                                                                                                                                                                                                |
|-------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 🎭 User Interface | [Jetpack Compose](https://developer.android.com/jetpack/compose) + [Compose Multiplatform](https://www.jetbrains.com/lp/compose-mpp/)                                                                                                    |
| 🏗 Architecture   | [MVVM](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)                                                                                                                                                               |
| 💉 DI             | [Koin](https://insert-koin.io/)                                                                                                                                                                                                          |
| 🛣️ Navigation    | [Compose Navigation](https://developer.android.com/jetpack/compose/navigation), [Decompose](https://arkivanov.github.io/Decompose/)                                                                                                      |
| 🌊 Async          | [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) + [Flow + StateFlow + SharedFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)                                       |
| 🌐 Networking     | [Ktor Client](https://ktor.io/docs/client.html)                                                                                                                                                                                          |
| 💵 Billing        | [RevenueCat](https://www.revenuecat.com/)                                                                                                                                                                                                |
| 📄 JSON           | [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)                                                                                                                                                                  |
| 💾 Persistence    | [SQLDelight](https://cashapp.github.io/sqldelight/), [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings)                                                                                                       |
| ⌨️ Logging        | [Timber](https://github.com/JakeWharton/timber) - Android, [slf4j + logback](https://www.baeldung.com/kotlin/logging), [Kermit](https://github.com/touchlab/Kermit)                                                                      |
| 📸 Image Loading  | [Coil](https://coil-kt.github.io/coil/)                                                                                                                                                                                                  |
| 🧪 Testing        | [Mockk](https://mockk.io/), [JUnit](https://junit.org/junit5/), [Turbine](https://github.com/cashapp/turbine), [Kotlin Test](https://kotlinlang.org/api/latest/kotlin.test/) + [Robolectric](https://github.com/robolectric/robolectric) |
| ⌚ Date and Time   | [Kotlinx DateTime](https://github.com/Kotlin/kotlinx-datetime)                                                                                                                                                                           |
| 🔐 Immutability   | [Kotlinx Immutable Collections](https://github.com/Kotlin/kotlinx.collections.immutable)                                                                                                                                                 |
| 🔧 Supplementary  | [Accompanist](https://github.com/google/accompanist)                                                                                                                                                                                     |

## Issues

If you encounter any issues you simply file them with the relevant details [here](https://github.com/ReluctApp/Reluct/issues/new/choose)

## 📃 Important Analysis

### 1. The purpose of this project

I made this project solely as a means of learning about Kotlin Multiplatform, Jetpack Compose and multi module project structuring.
I do not advocate for the use of this structure, especially for small projects. You can easily get similar results and re-usability with far less modules than this.
I made this to test the boundaries of Jetpack Compose and see how tooling works with Kotlin Multiplatform.
You can checkout a similarly structured project (with Compose Desktop already running) called [Thinkrchive](https://github.com/Thinkrchive/Thinkrchive-Multiplatform)

### 2. Evaluating Jetpack Compose on Android

There has been a lot of talk and doubt on Jetpack Compose and how production ready is it. After dealing with it in this project (I've used it other projects too). I can say the following;

#### i. Tooling
Tooling for Compose varies from easy to annoying.

- With Android Studio Dolphin we have full support for Layout Inspector and can see the number of recompositions happening in the app.
- We now have better support for Previews in Android Studio Electric Eel with a "hot reload like" feature upon code changes. The feature is called [Live Edit](https://developer.android.com/studio/preview/features#live_edit). 
  While its good it doesn't really compare to the convenience of previews in XML since there is no compilation involved there.
- Support for Animation Previews is a very big feature that I really enjoy using. But it faces the same issues of slow compilation on code changes as Previews
- No drag and drop like feature for UI components. It's hard to make such tool for Compose since it's Kotlin code that's adaptive.
  Some would argue that they never use drag and drop with XML but it's very much a deterring reason to some. At least we have [Relay](https://relay.material.io/) that can help you with some of this.

Most of the instability issues with the IDE have been fixed and it's very stable now but the slow Previews that can only be fixed by faster machines are still a very big issue to some developers.

#### ii. Going outside the box
Implementing designs that are different from the Material Design spec can vary from extremely easy to very hard.
Compose is very flexible and you will get more benefits if you are tasked with creating a design system. Compared to XML, custom designs are very easy in Compose and over great re-usability when done correctly.
I have a `components` [module](https://github.com/ReluctApp/Reluct/tree/main/android/compose/components) that has all the common custom components use throughout the app for easy re-usability and consistent design.

But it's not all rainbows, when you start doing custom things it can become tricky pretty fast.

**1. Making the bottom navigation bar collapsible on scroll or in some destinations was quite tricky**

You need the bottom nav bar to be at the top most `Scaffold` for the best effect, but hiding and showing it is based on the children screen below the top `Scaffold`
So what can you do to monitor the scroll of the different screens and decide when to hide or show the bar, while still maintaining readable code? 
Well you need to create something custom to do that for you. So, I had to create [BarsVisibility](https://github.com/ReluctApp/Reluct/blob/main/android/compose/components/src/main/kotlin/work/racka/reluct/android/compose/components/util/BarsVisibility.kt) and [ScrollContext](https://github.com/ReluctApp/Reluct/blob/main/android/compose/components/src/main/kotlin/work/racka/reluct/android/compose/components/util/LazyListUtils.kt)
```kotlin
// BarsVisibility interface
@Stable
interface BarVisibilityState {
  val isVisible: Boolean
  fun hide()
  fun show()
}

@Stable
interface BarsVisibility {
  val topBar: BarVisibilityState
  val bottomBar: BarVisibilityState

  // StatusBar and NavBar items are useful for triggering immersive mode
  val statusBar: BarVisibilityState
  val navigationBar: BarVisibilityState
}

// ScrollContext implementation
private fun LazyListState.isLastItemVisible(): Boolean =
  layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

private fun LazyListState.isFirstItemVisible(): Boolean =
  firstVisibleItemIndex == 0

data class ScrollContext(
  val isTop: Boolean,
  val isBottom: Boolean,
)

@Composable
fun rememberScrollContext(listState: LazyListState): ScrollContext {
  val scrollContext by remember {
    derivedStateOf {
      ScrollContext(
        isTop = listState.isFirstItemVisible(),
        isBottom = listState.isLastItemVisible()
      )
    }
  }
  return scrollContext
}
```
Simply observing `layoutInfo.visibleItemsInfo` or similar from `LazyListState` will cause random Recompositions. You need to get creative.
So, now you use them to hide or show the bottom nav bar without making it messy.
```kotlin
@Composable
fun MyApp() {
    val barsVisibility = rememberBarsVisibility()
    Scaffold(
      bottomBar = { BottomNavBar(show = barsVisibility.bottomBar.isVisible) }
    ) {
        AnimatedNavHost {
            composable {
                val listState = rememberLazyListState()
                val scrollContext = rememberScrollContenxt(listState)
                SideEffect {
                  if (scrollContext.isTop) {
                      barsVisibility.bottomBar.show()
                  } else {
                      barsVisibility.bottomBar.hide()
                  }
                }
              
                LazyList(listState) {
                    // Some Items Here
                }
            }
        }
    }
}
```
The issue with this is that hiding or showing the Bottom nav bar causes the whole screen to Recompose simply because that also changes the 
size of the parent `Scaffold` which causes re-calculation of its size. If most of Composables aren't [skippable](https://www.jetpackcompose.app/articles/donut-hole-skipping-in-jetpack-compose) then you are out of luck.
This is something I'm still exploring to see how I can fix it.

**2. Easily ending up with function having numerous parameters**

See [ScreenTimeStatisticsUI](https://github.com/ReluctApp/Reluct/blob/main/android/screens/src/main/kotlin/work/racka/reluct/android/screens/screentime/statistics/ScreenTimeStatisticsUI.kt) as an example.
If you really want to make sure you don't break [Unidirectional Data Flow](https://developer.android.com/jetpack/compose/architecture#udf) and don't pollute you all you child composables with `ViewModel` parameter that will cause multiple recompositions you need to State Hoist and end up with this;
```kotlin
@Composable
internal fun ScreenTimeStatisticsUI(
    modifier: Modifier = Modifier,
    barsVisibility: BarsVisibility,
    snackbarHostState: SnackbarHostState,
    uiState: ScreenTimeStatsState,
    getUsageData: (isGranted: Boolean) -> Unit,
    onSelectDay: (dayIsoNumber: Int) -> Unit,
    onUpdateWeekOffset: (weekOffsetValue: Int) -> Unit,
    onAppUsageInfoClick: (app: AppUsageInfo) -> Unit,
    onAppTimeLimitSettingsClicked: (packageName: String) -> Unit,
    onSaveAppTimeLimitSettings: (hours: Int, minutes: Int) -> Unit
)
```

**There are various other quirks like this that need you to come up with your own solution and make sure your solution doesn't drastically affect performance.**


#### iii. Performance
There are various articles that have discussed performance on Compose so I won't analyse much here. See [this](https://www.jetpackcompose.app/articles/donut-hole-skipping-in-jetpack-compose) article to know the common downfalls.

I can say that ensuring you have great performance on low end devices can become very hard in Jetpack Compose.
You need to know and use these;

- Using ImmutableList or ImmutableMap from [kotlinx.immutable.collections](https://github.com/Kotlin/kotlinx.collections.immutable)
- LaunchedEffect, SideEffect, DisposableEffect, remember, rememberSaveable, deriveStateOf, produceState.
- [State Hoisting](https://developer.android.com/jetpack/compose/state#state-hoisting)

[Thinking In Compose](https://developer.android.com/jetpack/compose/mental-model) guide can help you adjust your mental model but some say that this is a lot of caveats just to write UI differently
When using Compose you need to tread carefully or you might cause a significant performance problem. I even have some of these problems in this project.
This app performs well on most devices. Anything with the performance of Xiaomi Redmi 10C (SD 680) or Google Pixel 3a(SD 670) and higher should face no major performance issues, but it will struggle on low end devices.


**Performance is hard even with Views and XML but it's easier to mess up with Jetpack Compose**

#### iv. Animations

This is an area when I thing Compose excels imo. Most of the animations in this app are done with just;

- `AnimatedVisibility`
- `AnimatedConten`
- `animateFloatAsState`
- `animatedColorAsState`
- `Animatable`

With no fancy or complicated code but you still get great results.

#### v. Bugs, Experimental & Missing Features

Mostly it has been smooth sailing with very little bugs caused by Compose itself. 

But there are some major bugs that can make it not suitable for production completely. Some notable ones;

- [Keyboard gets closed when Text Field is scrolled slightly off screen](https://issuetracker.google.com/issues/179203700)
  You can reproduce this in this app. Open Tasks, Add New Task, Click the title text field, scroll a bit and the keyboard closes.
- You can't request focus for a field not in Composition yet (related to the issue above)
- [No full native auto fill support in TextFields](https://issuetracker.google.com/issues/176949051)

Then there's the issue of having a lot of `@ExperimentalXX` API that may not be acceptable in some companies and this immediately signals that this code is already technical debt.
Even stable versions of Compose have this problem.

Some important components are missing, though they are easy to replicate or find in the Accompanist library;

- Date & Time Picker
- Basic Graphs & Charts
- Dynamic Horizontal and Vertical pages
- Built-in image loading from URL
- System UI controller
- Smart Text wrapping in TextField
- More transition Animations in the official Navigation library (plenty of replacements though)
- More items in Long-Tap ContextMenu
- Remove Animations for LazyColumn
- [Material Motions](https://m2.material.io/design/motion/understanding-motion/)
- And others that I've probably forgotten

#### vi. Conclusions
While there might some issues in Jetpack Compose right now I think it's a great step toward native declarative UI in Android.
I look forward to more features and critical bug fixes on Compose so it can be feature parity with Views/XML. 

I will still keep using Compose for the right projects because it has made development and custom designs faster for me.
You'll benefit more from the speed of development in Compose when you define you build block components and use them instead of writing everything over and over.

### 3. Evaluating Kotlin Multiplatform

Kotlin Multiplatform is still in alpha stage. While Kotlin Multiplatform Mobile (Android + iOS) has been [announced going beta](https://blog.jetbrains.com/kotlin/2022/10/kmm-beta/#:~:text=Kotlin%20Multiplatform%20Mobile%27s%20promotion%20to,and%20gradually%20adopting%20Multiplatform%20Mobile.)
core Kotlin Multiplatform (Mobile + JVM + Linux + mingw/Windows + macOS/iOS native) is still very much alpha. There are companies that already embrace this alpha product like [Touchlab](https://touchlab.co/) and [Square/Block](https://kotlinlang.org/lp/mobile/case-studies/cash-app), adopting this will not be compelling to most companies.

I can say that there are some things worth noting before diving into it:

#### i. Tooling

IDE support is great at the moment but there are some major bugs that creep their way into it from time to time. Take the [KT48148](https://youtrack.jetbrains.com/issue/KT-48148/HMPP-Gradle-Unresolved-reference-to-any-class-from-kotlinxcoroutines-package-when-using) bug
as an example. It pretty much breaks all the smart IDE features and makes doing anything in the common code a hassle.
Right now (as of Nov-01/2022) we can't use Android Studio (Electric Eel and lower) without facing some variation of the bug mentioned above as it's only fixed in newer versions of the Intellij IDEA but at the same time Intellij IDEA does't support Android Gradle Plugin 7.3+. This makes it unsuitable for Android targets.

Tooling still has time to mature and I'd expect more collaboration between Jetbrains and Google as seen in this [Slack thread](https://kotlinlang.slack.com/archives/C3PQML5NU/p1652224566469179)

#### ii. Multiplatform UI

Kotlin Multiplatform is not aimed at being the next Flutter or React native and that means it does not force you to use a specific UI toolkit for your entire app.

It emphasizes more on sharing business logic to reduce replication on native code of the target platforms.
However, there is support for having common UI with [Compose Multiplatform](https://www.jetbrains.com/lp/compose-mpp/) where you share UI components.
For now we can share some UI components with Android, Desktop (JVM) and macOS/iOS (experimental). There is support for Compose Web but that doesn't share any components with the other platforms since it's based on the Web DOM.

Personally, I think sticking to just sharing business logic and may presentation logic (ViewModels or Presenters) is the sweet spot. There's still great value in writing the UI using platform specific toolkits. You will be able to adhere to platform UI and UX guidelines and still be able to make the app belong to platform. Not everyone want an iOS that looks like an Android one (since Compose Multiplatform use Material design as a base).

#### iii. Sharing business logic

The most compelling reason for Kotlin Multiplatform is the sharing of business logic. There are KMP ready libraries like SQLDelight, Multiplatform Settings, [Ktor Networking](https://ktor.io/), [Analytics Kotlin](https://github.com/segmentio/analytics-kotlin) and many more that let you write
everything in common code without having to make separate implementations. For things that are not supported you can easily make alternatives yourself with `expect/actual` or Interfaces.

- I've experimented with this for things like having a view model that can be used in common code but still get backed by platform specific implementations for easy use like [here](https://github.com/ReluctApp/Reluct/tree/main/common/mvvm-core).
```kotlin
// Define in commonMain
expect abstract class CommonViewModel() {
    val vmScope: CoroutineScope
    open fun onDestroy()
}

// Actual in jvmMain
actual abstract class CommonViewModel actual constructor() {
    actual val vmScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    actual open fun destroy() {
        vmScope.cancel()
    }
}

// Actual in androidMain
actual abstract class CommonViewModel actual constructor() : ViewModel() {
    actual val vmScope: CoroutineScope = viewModelScope
    actual open fun onDestroy() {
        vmScope.cancel()
    }
    // Cleared automatically by Android
    override fun onCleared() {
        super.onCleared()
        onDestroy()
    }
}
```
- Using this in `commonMain`

```kotlin
class MyFeatureViewModel : CommonViewModel() {
  fun someCall() {
    vmScope.launch {
      /** Do things here **/
    }
  }
}
```

- Or making an abstraction layer for things like [Billing](https://github.com/ReluctApp/Reluct/tree/main/common/billing) so you make using it in common code easier.
```kotlin
// Interface in commonMain
interface BillingApi {
    fun getProducts(): Result
    fun checkEntitlement(param: Type): Result?
    fun purchase(product: Product): Result
}

// implementation in androidMain
class AndroidBilling {
    /** Impl here **/
}

// implementation in jvmMain
class DesktopBilling {
    /** Impl here **/
}
```
- You can then use `BillingApi` in `commonMain` with no problem
```kotlin
// In commonMain
class ManageProducts(billing: BillingApi) {
    /** Do what you want here **/
}
```
Writing business logic once can be very beneficial for products that have a lot of business logic and depend on native platform development.

### 4. Broken tests

Some of the Unit tests in this project are broken. Why?
This project first started as an Android only project with some unit tests already written. Upon migration  migration to Kotlin Multiplatform I had
to rewrite all tests so they can be platform agnostic. This meant replacing all Junit with Kotlin-test, removing Hilt in favor of Koin and avoiding the use of Mockk.
During the migration refactoring became quite tricky and time consuming for something I was doing on in my spare time.
Android studio and Intellij IDEs became quite unstable with bugs like [KT48148](https://youtrack.jetbrains.com/issue/KT-48148/HMPP-Gradle-Unresolved-reference-to-any-class-from-kotlinxcoroutines-package-when-using) that
made IDE assisted refactoring impossible I quickly gave up and just YOLOed into feature completion. Tooling is still not great and refactoring tests is still tricky but
I am working on it in the [repair-tests](https://youtrack.jetbrains.com/issue/KT-48148/HMPP-Gradle-Unresolved-reference-to-any-class-from-kotlinxcoroutines-package-when-using) branch.
Any help will be appreciated.

## ✅ TODO

- Fix broken tests
- Add more Tests (UI Tests & Integration Tests)
- Add more features
- Support for more platforms

## 🙇 Credits

- Special thanks to [@theapache64](https://github.com/theapache64)
  for [readgen](https://github.com/theapache64/readgen)
- Thanks to all amazing people at Twitter for inspiring me to continue the development of this
  project.

## 🤝 Contributing

- See [CONTRIBUTING](/CONTRIBUTING.md)

## ❤ Show your support

Give a ⭐️ if this project helped you!

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/U6U44Y0MQ)

## 📝 License

- [Full License](/LICENSE)

```
Reluct - Tasks, Goals and Digital Wellbeing.
Copyright (C) 2022  racka98

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```

_**Made With ❤ From Tanzania 🇹🇿**_

_This README was generated by [readgen](https://github.com/theapache64/readgen)_ ❤
