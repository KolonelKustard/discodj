package com.totalchange.discodj.web.client.places;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

abstract class AbstractPlaceTokenizer<P extends Place> implements
        PlaceTokenizer<P> {
    private static final String PAIR_SPLIT = "&";
    private static final String NAME_VALUES_SPLIT = "=";
    private static final String VALUES_SPLIT = ",";

    protected abstract P getPlace(Map<String, List<String>> params);

    protected abstract Map<String, List<String>> getParams(P place);

    @Override
    public P getPlace(String token) {
        if (token == null || token.length() <= 0) {
            Map<String, List<String>> params = Collections.emptyMap();
            return getPlace(params);
        }

        String[] pairs = token.split(PAIR_SPLIT);
        Map<String, List<String>> params = new HashMap<String, List<String>>(
                pairs.length);
        for (String pair : pairs) {
            String[] namesAndValues = pair.split(NAME_VALUES_SPLIT);
            String name = null;
            String valuesStr = null;

            if (namesAndValues.length > 0) {
                name = URL.decodeQueryString(namesAndValues[0]);
            } else {
                continue;
            }

            if (namesAndValues.length > 1) {
                valuesStr = namesAndValues[1];
            }

            List<String> valuesList = params.get(name);
            if (valuesList == null) {
                valuesList = new ArrayList<String>();
                params.put(name, valuesList);
            }

            if (valuesStr != null) {
                String[] valuesArr = valuesStr.split(VALUES_SPLIT);
                for (String value : valuesArr) {
                    valuesList.add(URL.decodeQueryString(value));
                }
            }
        }

        return getPlace(params);
    }

    @Override
    public String getToken(P place) {
        StringBuilder str = new StringBuilder();
        Map<String, List<String>> params = getParams(place);

        for (Map.Entry<String, List<String>> param : params.entrySet()) {
            if (str.length() > 0) {
                str.append(PAIR_SPLIT);
            }
            str.append(URL.encodeQueryString(param.getKey()));

            if (param.getValue() != null && !param.getValue().isEmpty()) {
                str.append(NAME_VALUES_SPLIT);
                for (int num = 0; num < param.getValue().size(); num++) {
                    if (num > 0) {
                        str.append(VALUES_SPLIT);
                    }
                    str.append(URL.encodeQueryString(param.getValue().get(num)));
                }
            }
        }

        return str.toString();
    }
}
