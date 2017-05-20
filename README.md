<div align="center"><img src="/screens/gif_cover.gif"/></div>

# RotatingText
[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16s)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![by-SDS-MDG](https://img.shields.io/badge/by-SDS%2C%20MDG-blue.svg)](https://mdg.sdslabs.co)

Rotating text is an android library that can be used to make text switching painless and beautiful, with he use of interpolators, typefaces and more customisations.

# Usage
Just add the following dependency in your app's `build.gradle`
```
dependencies {
      compile 'com.sdsmdg.harjot:rotating-text:0.9.0'
}
```

## Example Usage 1 (Simple)
#### XML

```
<com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper
        android:id="@+id/custom_switcher"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
```

#### Java

```
RotatingTextWrapper rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher);
rotatingTextWrapper.setSize(35);

Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Word", "Word01", "Word02");
rotatable.setSize(35);
rotatable.setAnimationDuration(500);

rotatingTextWrapper.setContent("This is ?", rotatable);
```

#### Result
<img src="/screens/gif_example_1.gif"/>

## Example Usage 2 (Typeface + Interpolator)
#### XML

```
<com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper
        android:id="@+id/custom_switcher"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
```

#### Java

```
Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");
Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Reckoner_Bold.ttf");

RotatingTextWrapper rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher);
rotatingTextWrapper.setSize(35);
rotatingTextWrapper.setTypeface(typeface2);

Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Word", "Word01", "Word02");
rotatable.setSize(35);
rotatable.setAnimationDuration(500);
rotatable.setTypeface(typeface);
rotatable.setInterpolator(new BounceInterpolator());

rotatingTextWrapper.setContent("This is ?", rotatable);
```

#### Result
<img src="/screens/gif_example_2.gif"/>

## Example Usage 3 (Multiple Rotatables)
#### XML

```
<com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper
        android:id="@+id/custom_switcher"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
```

#### Java

```
Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");
Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Reckoner_Bold.ttf");

RotatingTextWrapper rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher);
rotatingTextWrapper.setSize(35);
rotatingTextWrapper.setTypeface(typeface2);

Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Word", "Word01", "Word02");
rotatable.setSize(35);
rotatable.setTypeface(typeface);
rotatable.setInterpolator(new AccelerateInterpolator());
rotatable.setAnimationDuration(500);

Rotatable rotatable2 = new Rotatable(Color.parseColor("#123456"), 1000, "Word03", "Word04", "Word05");
rotatable2.setSize(25);
rotatable2.setTypeface(typeface);
rotatable2.setInterpolator(new DecelerateInterpolator());
rotatable2.setAnimationDuration(500);

rotatingTextWrapper.setContent("This is ? and ?", rotatable, rotatable2);
```

#### Result
<img src="/screens/gif_example_3.gif"/>

# License
RotatingText is licensed under `MIT license`. View [license](LICENSE.md).
