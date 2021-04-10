# Debug Drawer for Jetpack Compose

### Versions

![drawer-base](https://img.shields.io/badge/Base-0.6.0-brightgreen)
![drawer-modules](https://img.shields.io/badge/Modules-0.6.0-brightgreen)
![drawer-ui-modules](https://img.shields.io/badge/UI_Modules-0.6.0-brightgreen)

![Compatible with Compose](https://img.shields.io/badge/Compose-1.0.0--beta04-brightgreen)

Composable Debug Drawer for Jetpack Compose apps

<img width="250" src="art/drawer_demo.png" />

## Install

```groovy
allprojects {
    repositories {
        //...
        mavenCentral()
    }
}
```

Add dependencies:

```groovy
implementation 'com.github.alorma:drawer-base:$version'
implementation 'com.github.alorma:drawer-modules:$version'
implementation 'com.github.alorma:drawer-ui-modules:$version'
```

## Setup

Wrap your content with `DebugDrawerLayout`:

```kotlin
DebugDrawerLayout(
    drawerModules = {
        TODO()
    }
) {
    // TODO Add your APP Content here
}
```

### Debug vs Release

If you don't want DebugDrawer layout code in release you can wrap it on a custom function:

`src/debug/...`

```kotlin
@Composable
fun ConfigureScreen(bodyContent: @Composable () -> Unit) {
    DebugDrawerLayout(
        drawerModules = { ... },
        bodyContent = { bodyContent() },
    )
}
```

`src/release/...`

```kotlin
@Composable
fun ConfigureScreen(bodyContent: @Composable () -> Unit) {
    bodyContent()
}
```

## Modules

Add modules as a list of `DebugModule`s

```kotlin
DebugDrawerLayout(
    debug = { BuildConfig.DEBUG },
    drawerModules = {
        DeviceModule()
        BuildModule()
    }
) {
    // TODO Add your APP Content here
}
```

#### Actions Module

This module has a composable slot.

You can build any of the provided actions, or build your own.

<img width="160" src="art/actions_module.png" />

*Actions*

* ButtonAction: Shows a `Button` with given text, and register a lambda to receive its click

* SwitchAction: Shows a `Switch` and register a lambda to receive its changes

#### Build Module

Shows information about the app: Version code, Version name and Package

<img width="160" src="art/build_module.png" />

#### Device Module

Shows information about device running the app such as model and manufacturer

<img width="160" src="art/device_module.png" />

#### Design Module

Allows to show a Grid layer as overlay of your content, to help align content to grid

<img width="160" src="art/design_module_1.png" />
<img width="160" src="art/design_module_2.png" />

```kotlin
var debugGridLayerConfig: DebugGridStateConfig by remember {
    mutableStateOf(DebugGridStateConfig())
}

DebugDrawerLayout(
    drawerModules = {
        //...
        DesignModule(config = debugGridLayerConfig) {
            debugGridLayerConfig = it
        }
        //...
    },
    bodyContent = { drawerState ->
        Box {
            // Body of your app
            DebugGridLayer(debugGridLayerConfig)
        }
    },
)
```

#### Custom Module

Debug drawer can show any `@Composable` function.

If you want to provide a custom module that looks like the ones provided by the library:

```kotlin
@Composable
fun CustomModule(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: String,
    items: List<Pair<String, String>>
) {
    DebugDrawerModule(
        modifier = modifier,
        icon = icon,
        title = title
    ) {
        // Module content
    }
}
```

## Theming && Customization

Use `drawerColors` to customize drawer theme colors.

```kotlin
DebugDrawerLayout(
    drawerColors = YourColorScheme, // darkColors() or lightColors()
)
```

If you want to modify the drawer colors, use `DebugDrawerDefaults.colors.copy(...)`

### Modules list UI

Update module UI by pass `Modifier`

```kotlin
DebugDrawerLayout(
    drawerModules = {
        val modulesModifier = Modifier
            .padding(4.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colors.surface)
        DeviceModule(modulesModifier)
        BuildModule(modulesModifier)
    }
)
```
