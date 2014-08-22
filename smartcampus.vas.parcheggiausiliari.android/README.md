smartcampus.vas.parcheggiausiliari.android
==============================

##App which eases the reporting of occupied parkings in Rovereto

The user can make a report for a street or a parking by clicking on the street itself or on the marker of the parking, a popup will open showing the last report.
If the users clicks on one of the two buttons on the bottom of the screen instead a list will show up and if an item is clicked his page will show up


##It consists in 2 activities:
	- LoginActivity				which is showed only at the first boot of the app and every time the user logs out
	- MainActivity				which acts as a container for all the fragments 

##The fragments that compose the application are:

	-	MapFragment										which shows data on the map and lets the user search for a specific Street/Parking
	
	-	ParkListFragment and StreetListFragment			are simple lists containing respectively only the Parkings or the Streets
	
	-	DetailsFragment									which acts as a placeholder for the tabs which are composed by:
			-	SegnalaFragment									is the fragment containing all the pickers to make a report
			-	StoricoFragment									shows last reports of a Street/Parking
		
	-	StoricoAgenteFragment							shows last ten reports made by a user
	
##Known problems:
	
	-	Sometimes when the user returns to a previous page the views glitches showing the two Fragments overlapping. A restart of the application is needed.