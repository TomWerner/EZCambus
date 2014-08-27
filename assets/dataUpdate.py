import urllib2
import json

def getRouteListJson():
    url = "http://api.ebongo.org/routelist?format=json" + \
          "&api_key=hfjiLcSjgJUQnhqn1WpGu4AfKBZX7Eo1"
    data = urllib2.urlopen(url).read()
    data = json.loads(data)
    return data

def getStopListJson():
    url = "http://api.ebongo.org/stoplist?format=json" + \
          "&api_key=hfjiLcSjgJUQnhqn1WpGu4AfKBZX7Eo1"
    data = urllib2.urlopen(url).read()
    data = json.loads(data)
    return data

def getRouteInfoJson(agency, tag):
    url = "http://api.ebongo.org/route?format=json&agency=" + agency + \
          "&route=" + tag + "&api_key=hfjiLcSjgJUQnhqn1WpGu4AfKBZX7Eo1"
    data = urllib2.urlopen(url).read()
    data = json.loads(data)
    return data

def getStopInfoJson(stopid):
    url = "http://api.ebongo.org/stop?format=json&stopid=" + stopid + \
          "&api_key=hfjiLcSjgJUQnhqn1WpGu4AfKBZX7Eo1"
    data = urllib2.urlopen(url).read()
    data = json.loads(data)
    return data

def getRouteData():
    data = getRouteListJson()
    total = len(data.get("routes"))
    count = 0
    for route in data.get("routes"):
        num = int((float(count) / float(total)) * 100)
        if (num % 10 == 0):
            print num
        count = count + 1
        route = route.get("route")
        
        route["n"] = route.get("name")
        del route["name"]
        
        route["t"] = route.get("tag")
        del route["tag"]
        
        route["a"] = route.get("agency")
        del route["agency"]

        details = getRouteInfoJson(route.get("a"), route.get("t")).get("route")
        route["n"] = details.get("name")

        directions = details.get("directions")
        route["d"] = []
        for direc in directions:
            entry = {}
            entry["d"] = direc.get("direction")
            entry["tag"] = direc.get("directiontag")

            stops = direc.get("stops")
            entry["s"] = []
            for stop in stops:
                entry["s"].append(stop.get("stopnumber"))
            route["d"].append(entry)

        #paths = details.get("paths")
        #route["p"] = []
        #for path in paths:
        #    entry = {}
        #    entry["t"] = path.get("tag")
        #    entry["p"] = []
        #    for point in path.get("points"):
        #        pnt = {}
        #        pnt["lat"] = point.get("lat")
        #        pnt["lng"] = point.get("lng")
        #        entry["p"].append(pnt)
        #    route["p"].append(entry)
    return data

def getStopData():
    data = getStopListJson()
    total = len(data.get("stops"))
    count = 0
    for stop in data.get("stops"):
        num = int((float(count) / float(total)) * 100)
        if (num % 10 == 0):
            print num
        count = count + 1
        stop = stop.get("stop")

        stop["n"] = stop.get("stopnumber")
        del stop["stopnumber"]

        stop["t"] = stop.get("stoptitle")
        del stop["stoptitle"]

        stop["lat"] = stop.get("stoplat")
        del stop["stoplat"]

        stop["lng"] = stop.get("stoplng")
        del stop["stoplng"]

        details = getStopInfoJson(stop["n"]).get("stopinfo")
        stop["t"] = details.get("stoptitle")
        stop["lat"] = details.get("latitude")
        stop["lng"] = details.get("longitude")

        stop["r"] = []
        for route in details.get("routes"):
            stop["r"].append(route.get("tag"))
    return data


out1 = open(r'C:\Users\Tom\workspace\EZCambus\assets\route_data.json', 'w')
data1 = getRouteData();
out1.write(json.dumps(data1, ensure_ascii=False))
out1.close()

out2 = open(r'C:\Users\Tom\workspace\EZCambus\assets\stop_data.json', 'w')
data2 = getStopData();
out2.write(json.dumps(data2, ensure_ascii=False))
out2.close()
