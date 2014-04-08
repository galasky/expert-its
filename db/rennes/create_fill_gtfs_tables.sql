CREATE TABLE agency (agency_id TEXT, agency_name TEXT, agency_url TEXT, agency_timezone TEXT, agency_lang TEXT, agency_phone TEXT);
CREATE TABLE stops (stop_id TEXT, stop_code INTEGER, stop_name TEXT, stop_desc TEXT, stop_lat REAL, stop_lon REAL, zone_id TEXT, stop_url TEXT, location_type INTEGER, parent_station INTEGER, stop_timezone TEXT, wheelchair_boardin INTEGER);
CREATE TABLE routes (route_id TEXT, agency_id TEXT, route_short_name TEXT, route_long_name TEXT, route_desc TEXT, route_type INTEGER, route_url TEXT, route_color TEXT, route_text_color TEXT);
CREATE TABLE trips (route_id TEXT, service_id TEXT, trip_id TEXT, trip_headsign TEXT, trip_short_name TEXT, direction_id TEXT, block_id TEXT, shape_id TEXT, wheelchair_accessible INTEGER, bikes_allowed INTEGER);
CREATE TABLE stop_times (trip_id TEXT, arrival_time TEXT, departure_time TEXT, stop_id TEXT, stop_sequence INTEGER, stop_headsign TEXT, pickup_type INTEGER, drop_off_type INTEGER, shape_dist_traveled REAL);
CREATE TABLE calendar (service_id TEXT, monday INTEGER, tuesday INTEGER, wednesday INTEGER, thursday INTEGER, friday INTEGER, saturday INTEGER, sunday INTEGER, start_date TEXT, end_date TEXT);
CREATE TABLE calendar_dates (service_id TEXT, date TEXT, exception_type INTEGER);
CREATE TABLE fare_attributes (fare_id TEXT, price REAL, currency_type TEXT, payment_method INTEGER, transfers INTEGER, transfer_duration INTEGER);
CREATE TABLE fare_rules (fare_id TEXT, route_id TEXT, origin_id TEXT, destination_id TEXT, contains_id TEXT);
CREATE TABLE shapes (shape_id TEXT, shape_pt_lat REAL, shape_pt_lon REAL, shape_pt_sequence INTEGER, shape_dist_traveled REAL);
CREATE TABLE frequencies (trip_id TEXT, start_time TEXT, end_time TEXT, shapes INTEGER, exact_times REAL);
CREATE TABLE transfers (from_stop_id TEXT, to_stop_id TEXT, transfer_type INTEGER, min_transfer_time INTEGER);
CREATE TABLE feed_info (feed_publisher_name TEXT, feed_publisher_url TEXT, feed_lang TEXT, feed_start_date TEXT, feed_end_date TEXT, feed_version TEXT);
.separator ,
.import agency.txt agency
.import stops.txt stops
.import routes.txt routes
.import trips.txt trips
.import stop_times.txt stop_times
.import calendar.txt calendar
.import calendar_dates.txt calendar_dates
.import fare_attributes.txt fare_attributes
.import fare_rules.txt fare_rules
.import shapes.txt shapes
.import frequencies.txt frequencies
.import transfers.txt transfers
.import feed_info.txt feed_info
