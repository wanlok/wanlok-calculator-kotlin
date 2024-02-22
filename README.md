This is an app I developed to practise Android MVVM with data binding and Google Firestore.

I notice a few pain points in calculator apps available in Google Play and App Store. Therefore, I decided to write my calculator app to solve those issues and make it open-source.

APK Download:

https://drive.google.com/file/d/1GmGS60SCfCAcpNZRhfePR5V6GegH2065/view?usp=sharing

1. The main user interface display operands vertically, user can remove any lines by swiping them to the right

![1](https://github.com/wanlok/wanlok-calculator-kotlin/assets/48524179/6681f75e-1bf3-4004-98c1-aa23ca264575)

2. Users can convert numbers into units maintained on Firestore.

![2](https://github.com/wanlok/wanlok-calculator-kotlin/assets/48524179/a0ecd1ec-2878-413c-9cd2-b61b9d5fd92c)

3. Formulas for conversion are stored in Firestore. This allow new conversion to be added dynamically to the app in the future. A parser is implemented to parse strings into math equations and calculate their answers.

![Firestore](https://github.com/wanlok/wanlok-calculator-kotlin/assets/48524179/40ff23fb-42b6-4f71-a93f-68886643432a)

Credits:

<a href="https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form">How to evaluate a math expression given in string form?</a>

<a href="https://www.flaticon.com/free-icons/calculator" title="calculator icons">Calculator icons created by Hilmy Abiyyu A. - Flaticon</a>

<a href="https://www.flaticon.com/free-icons/convert" title="convert icons">Convert icons created by sonnycandra - Flaticon</a>

<a href="https://www.flaticon.com/free-icons/save" title="save icons">Save icons created by Mihimihi - Flaticon</a>

<a href="https://www.flaticon.com/free-icons/open-folder" title="open-folder icons">Open-folder icons created by kmg design - Flaticon</a>
