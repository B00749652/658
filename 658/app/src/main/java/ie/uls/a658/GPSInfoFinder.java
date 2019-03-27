package ie.uls.a658;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GPSInfoFinder {

    Map<String, GPS> cluster = new HashMap<>();
    String[] locations = {"Parnell Street", "Temple Bar", "Phibsborough", "Donnybrook", "Raheny", "Clonskeagh", "Cabra", "Inchicore", "DCU", "Ballyfermot", "St Stephens Green"};

    GPSInfoFinder() {
        cluster.put("Parnell Street", new GPS(53.3518327, -6.2650986));
        cluster.put("Temple Bar", new GPS(53.3454357, -6.266874));
        cluster.put("Phibsborough", new GPS(53.3599177, -6.2811761));
        cluster.put("Donnybrook", new GPS(53.3192706, -6.2407931));
        cluster.put("Raheny", new GPS(53.3804184, -6.1838705));
        cluster.put("Clonskeagh", new GPS(53.3084935, -6.2506413));
        cluster.put("Cabra", new GPS(53.3582121, -6.2950004));
        cluster.put("Inchicore", new GPS(53.3335384, -6.3373976));
        cluster.put("DCU", new GPS(53.387612, -6.258347));
        cluster.put("Ballyfermot", new GPS(53.3406391, -6.3589917));
        cluster.put(" St Stephens Green", new GPS(53.3421928, -6.2647287));

    }

    public int getNearestCluster(double lat, double lng) {
        Map<String, Integer> distances = new HashMap<>();
        Iterator<String> it = cluster.keySet().iterator();
        double lat2 = Math.toRadians(lat);
        double lng2 = Math.toRadians(lng);
        while (it.hasNext()) {
            String key = it.next();
            double latitude = cluster.get(key).getLat();
            double longitude = cluster.get(key).getLng();
            double lat1 = Math.toRadians(latitude);
            double lng1 = Math.toRadians(longitude);
            double phi = lat1 - lat2;
            double lambda = lng1 - lng2;
            double x = 6371000 * phi;
            double y = 6371000 * lambda * Math.cos(lat2);
            int calc = (int) (Math.round(Math.sqrt(y * y + x * x) * 10) / 10);
            distances.put(key, calc);
        }

            Map.Entry<String, Integer> min = null;
            for (Map.Entry<String, Integer> k : distances.entrySet()) {
                Map.Entry<String, Integer> temp = k;
                if ((min == null) || (min.getValue() > k.getValue())) {
                    min = temp;
                }
                //  answer = min.getKey();
            }
        int answer = 0;
        for(int count = 0; count < locations.length; count++){
            if(min.getKey().equals(locations[count])){
                answer = count;
                break;
            }
        }

        return answer;
    }

}//class