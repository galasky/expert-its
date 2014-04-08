CREATE TABLE agency (agency_id INTEGER, agency_name TEXT, agency_url TEXT, agency_timezone TEXT, agency_phone TEXT, agency_lang TEXT);
CREATE TABLE stops (stop_id INTEGER, stop_code INTEGER, stop_name TEXT, stop_desc TEXT, stop_lat REAL, stop_lon REAL, zone_id INTEGER, stop_url TEXT, location_type INTEGER, parent_station INTEGER, stop_timezone TEXT, wheelchair_boardin INTEGER);
CREATE TABLE routes (route_id INTEGER, agency_id INTEGER, route_short_name TEXT, route_long_name TEXT, route_desc TEXT, route_type INTEGER, route_url TEXT, route_color TEXT, route_text_color TEXT);
CREATE TABLE trips (trip_id INTEGER, service_id INTEGER, route_id INTEGER, trip_headsign TEXT, direction_id INTEGER, block_id INTEGER);
CREATE TABLE stop_times (trip_id INTEGER, stop_id INTEGER, stop_sequence INTEGER, arrival_time TEXT, departure_time TEXT, stop_headsign TEXT, pickup_type INTEGER, drop_off_type INTEGER, shape_dist_traveled REAL);
CREATE TABLE calendar (service_id INTEGER, monday INTEGER, tuesday INTEGER, wednesday INTEGER, thursday INTEGER, friday INTEGER, saturday INTEGER, sunday INTEGER, start_date TEXT, end_date TEXT);
CREATE TABLE calendar_dates (service_id INTEGER, date TEXT, exception_type INTEGER);
.separator ,
.import calendar.txt calendar
.import agency.txt agency
.import calendar_dates.txt calendar_dates
.import routes.txt routes
.import stop_times.txt stop_times
.import stops.txt stops
.import trips.txt trips
