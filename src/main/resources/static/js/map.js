// map.js
document.addEventListener("DOMContentLoaded", function () {
    // Initialize the map
    var map = L.map('map').setView([latitude, longitude], zoomLevel);

    // Add a tile layer to the map (e.g., OpenStreetMap)
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
        maxZoom: 18,
    }).addTo(map);

    // Add a marker to the map at the specified coordinates
    var marker;

    function addMarker(e) {
        // Remove the existing marker, if any
        if (marker) {
            map.removeLayer(marker);
        }

        // Get the clicked coordinates
        var latLng = e.latlng;
        var latitude = latLng.lat;
        var longitude = latLng.lng;

        // Add a new marker at the clicked location
        marker = L.marker([latitude, longitude]).addTo(map);

        // Set the latitude and longitude values in the input fields
        document.getElementById("latitude").value = latitude;
        document.getElementById("longitude").value = longitude;
    }

    map.on('click', addMarker);
});
