# Project 2 - NY Times Search App

NY Times Search App is an android app that allows a user to search for articles on web using simple filters. The app utilizes [New York Times Search API](http://developer.nytimes.com/docs/read/article_search_api_v2).

Time spent: 40 hours spent in total

## User Stories

The following **required** functionality is completed:

* User can **search for news article** by specifying a query and launching a search. Search displays a grid of image results from the New York Times Search API.
* User can click on "filter" which allows selection of **advanced search options** to filter results
* User can configure advanced search filters such as:
  * Begin Date (using a date picker)
  * News desk values (Arts, Fashion & Style, Sports)
  * Sort order (oldest or newest)
* Subsequent searches have any filters applied to the search results
* User can tap on any article in results to view the contents in an embedded browser.
* User can **scroll down to see more articles**. The maximum number of articles is limited by the API search.

The following **optional** features are implemented:

* Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* Used the **ActionBar SearchView** or custom layout as the query box instead of an EditText
* User can **share an article link** to their friends or email it to themselves
* Replaced Filter Settings Activity with a lightweight modal overlay
* Improved the user interface and experiment with image assets and/or styling and coloring

The following **bonus** features are implemented:

* Use the [RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) with the `StaggeredGridLayoutManager` to display improve the grid of image results
* For different news articles that only have text or only have images, use [Heterogenous Layouts](http://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView) with RecyclerView
* Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
* Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.com/android/Drawables#vector-drawables) where appropriate.
* Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* Uses [retrolambda expressions](http://guides.codepath.com/android/Lambda-Expressions) to cleanup event handling blocks.
* Replace the embedded `WebView` with [Chrome Custom Tabs](http://guides.codepath.com/android/Chrome-Custom-Tabs) using a custom action button for sharing.

The following **additional** features are implemented:

* Added Snackbar to show the internet/network failure nessages. You can also click RETRY to fetch the contents.
* Added CardViews to the StaggeredGridLayoutManager to make it more appealing

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/aditilonhari/NYTimesSearch/blob/master/nytimes.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />


## Notes

Describe any challenges encountered while building the app.
- Adding filters to include the  non-alpha, non-numeric characters was an issue. Using URIEncoder solved the issue.
- Sharing the link for an implicit intent in a chrome tab had clashes with the inbuilt chrome sharing option.
- Modal Overlay sizing was an issue, used Linear layout to fix that.
- StaggeredGrid view setting were difficult to manage. Found Android Cardview option which was much easier to implement and help
- Understanding the working of Data binding was challenging initially.
- Getting Data Binding and Heterogenous layouts to work was a task

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Android CardView] (https://developer.android.com/reference/android/support/v7/widget/CardView.html) - To make better GridView displays
- [Android Data Binding Library] (https://developer.android.com/topic/libraries/data-binding/index.html) - Connect views with data in a much more powerful way
- [Java8 Lamba Expressions] (https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html) - Helps eliminate boilerplate code that makes the syntax verbose and less clear
- [Parceler](https://parceler.org/) - Third-party library to automate the boilerplate code work in [Parcelable](https://developer.android.com/reference/android/os/Parcelable.html)
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) - Image loading and caching library for Android (Much faster and efficient than Picasso)

## License


    Copyright 2016 Aditi Lonhari

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
