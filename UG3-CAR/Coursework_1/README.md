####   Computer Architecture 2014 - University of Edinburgh
####   Student ID: s1115104

Both types of predictors are implemented.

To run Static predictors execute the following:

Always Taken Branch Predictor
  python2.7 static_predictor.py <file_name> alwaystaken

Always Not Taken Branch Predictor
	python2.7 static_predictor.py <file_name> nevertaken

Profile Guided Branch Predictor
	python2.7 static_predictor.py <file_name> profile


To run Adaptive Predictor, execute the following:

python2.7 adaptive_predictor.py <file_name> <history>

where history is the length of history to keep for each address.
