# corona_monitoring_platform

As an implmentation for the software engineering, it was assigned to implement COVID-19 Statistics Monitoring Platform.  
| <img src="https://user-images.githubusercontent.com/61229902/171127035-fde9ab4d-ca43-41a1-b9aa-1a259ab95abc.png"  width="40%"  class="img-responsive"> |
|:---:|
| The Android Application Home Page |

## Overview 
Software Engineering Project to plot and visualize daily and comulative data regarding COVID-19 based on REST APIs and native android application development. 

## Design Requirements
An interactive responsive user interface asking the website/app user to choose his/her preferences for viewing the data (country,..), (daily data, cumulative data,...), (type of visualization as numbers or graphs). A reliable backend managing the data retrieval and the routing between pages. Test methods verifying the functionality and validity of the website/application.

## APIs
The application relied on multiple APIs for the variety of purposes, one to visualize the cumlative COVID-19 data for a given period regarding an input country, while  other two to retriece daily data. 

## Project Extension
The dash board program was extended to estimate the virus basic reproduction number $R_0$. It was assumed that : <br>
The total number of infection at day $t$ is $n(t)$; and that the average duration before being infectous is $τ$ . Then: 
$$n(t) = n(0)R_0^{t/τ}$$

Taking log, we get

$$log n(t) = log n(0) + t/τ log R_0$$
Taking that $τ = 6$, and use the data you collected for $n(t)$ and $n(0)$, and
implmented a script to fit the equation using least squares method, given any chosen country or worldwide.
