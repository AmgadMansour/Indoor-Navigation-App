# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)

# # Graph
# Graph.create(json: '{"vertices":{"n1":{"x":126.61296081542969,"y":1451},"n2":{"x":123.24651336669922,"y":1372},"n3":{"x":127,"y":1259.7137451171875},"n4":{"x":123.49036407470703,"y":1068},"n5":{"x":267,"y":1064.04541015625},"n6":{"x":259,"y":856},"n7":{"x":47,"y":1368},"n8":{"x":47,"y":1268.172119140625},"n9":{"x":163,"y":848},"n10":{"x":267,"y":640},"n11":{"x":227,"y":521},"n12":{"x":355,"y":505},"n13":{"x":495,"y":501},"n14":{"x":319,"y":760},"n15":{"x":387,"y":760},"n16":{"x":455,"y":744},"n17":{"x":527,"y":748},"n18":{"x":595,"y":752},"n19":{"x":694,"y":760.1246948242188},"n20":{"x":766,"y":748},"n21":{"x":830,"y":748},"n22":{"x":898,"y":748},"n23":{"x":974,"y":736},"n24":{"x":1042,"y":732},"n25":{"x":647,"y":505},"n26":{"x":798,"y":493},"n27":{"x":930,"y":485},"n28":{"x":1030,"y":489},"n29":{"x":427,"y":636.6154174804688},"n30":{"x":563,"y":632},"n31":{"x":647,"y":632},"n32":{"x":770,"y":632},"n33":{"x":866,"y":631.7191162109375},"n34":{"x":1002,"y":630.9849243164063},"n35":{"x":187,"y":608},"n36":{"x":315,"y":672},"n37":{"x":315,"y":612},"n38":{"x":407,"y":672},"n39":{"x":451,"y":676},"n40":{"x":455,"y":604},"n41":{"x":535,"y":672},"n42":{"x":591,"y":672},"n43":{"x":710,"y":672},"n44":{"x":766,"y":672},"n45":{"x":846,"y":672},"n46":{"x":894,"y":672},"n47":{"x":986,"y":668},"n48":{"x":1034,"y":672},"n49":{"x":1034,"y":600.9783325195313},"n50":{"x":894,"y":597},"n51":{"x":750.0148315429688,"y":604},"n52":{"x":623,"y":581}},"edges":[{"source":"n2","target":"n1"},{"source":"n3","target":"n2"},{"source":"n4","target":"n3"},{"source":"n5","target":"n4"},{"source":"n6","target":"n5"},{"source":"n10","target":"n6"},{"source":"n29","target":"n10"},{"source":"n30","target":"n29"},{"source":"n31","target":"n30"},{"source":"n32","target":"n31"},{"source":"n33","target":"n32"},{"source":"n34","target":"n33"},{"source":"n7","target":"n2"},{"source":"n8","target":"n3"},{"source":"n9","target":"n6"},{"source":"n35","target":"n10"},{"source":"n11","target":"n35"},{"source":"n37","target":"n10"},{"source":"n12","target":"n37"},{"source":"n36","target":"n10"},{"source":"n14","target":"n36"},{"source":"n38","target":"n29"},{"source":"n15","target":"n38"},{"source":"n39","target":"n29"},{"source":"n16","target":"n39"},{"source":"n40","target":"n29"},{"source":"n13","target":"n40"},{"source":"n41","target":"n30"},{"source":"n42","target":"n30"},{"source":"n17","target":"n41"},{"source":"n18","target":"n42"},{"source":"n52","target":"n31"},{"source":"n25","target":"n52"},{"source":"n43","target":"n31"},{"source":"n19","target":"n43"},{"source":"n44","target":"n32"},{"source":"n20","target":"n44"},{"source":"n51","target":"n32"},{"source":"n26","target":"n51"},{"source":"n45","target":"n33"},{"source":"n21","target":"n45"},{"source":"n46","target":"n33"},{"source":"n22","target":"n46"},{"source":"n50","target":"n33"},{"source":"n27","target":"n50"},{"source":"n49","target":"n34"},{"source":"n28","target":"n49"},{"source":"n47","target":"n34"},{"source":"n23","target":"n47"},{"source":"n48","target":"n34"},{"source":"n24","target":"n48"}]}', map_id: 1)
#
# # Categories
# Category.create(id: 1, name: 'Mobile Apps', map_id: 1)
# Category.create(id: 2, name: 'Test Suites', map_id: 1)
# Category.create(id: 3, name: 'Universities', map_id: 1)
# Category.create(id: 4, name: 'Cloud Computing', map_id: 1)
#
#
# # Destinations
# Destination.create(name: 'D1', description: 'D1', x_coordinate: 227.0, y_coordinate: 521.0, map_id: 1, category_id: 1)
# Destination.create(name: 'D2', description: 'D2', x_coordinate: 319.0, y_coordinate: 760.0, map_id: 1, category_id: 1)
# Destination.create(name: 'D3', description: 'D3', x_coordinate: 387.0, y_coordinate: 760.0, map_id: 1, category_id: 1)
#
# Destination.create(name: 'D4', description: 'D4', x_coordinate: 647.0, y_coordinate: 505.0, map_id: 1, category_id: 2)
# Destination.create(name: 'D5', description: 'D5', x_coordinate: 798.0, y_coordinate: 493.0, map_id: 1, category_id: 2)
#
#
# Destination.create(name: 'D6', description: 'D6', x_coordinate: 355.0, y_coordinate: 505.0, map_id: 1, category_id: 3)
# Destination.create(name: 'D7', description: 'D7', x_coordinate: 495.0, y_coordinate: 501.0, map_id: 1, category_id: 3)
# Destination.create(name: 'D8', description: 'D8', x_coordinate: 930.0, y_coordinate: 485.0, map_id: 1, category_id: 3)
#
#
# Destination.create(name: 'D9', description: 'D9', x_coordinate: 595.0, y_coordinate: 752.0, map_id: 1, category_id: 4)
# Destination.create(name: 'D10', description: 'D10', x_coordinate: 694.0, y_coordinate: 760.1247, map_id: 1, category_id: 4)
# Destination.create(name: 'D11', description: 'D11', x_coordinate: 766.0, y_coordinate: 748.0, map_id: 1, category_id: 4)
#
# Transition Nodes
# Transition.create(x_coordinate: 123.24651, y_coordinate: 1372.0, map_id: 1)
# # Transition.create(x_coordinate: 163.0, y_coordinate: 848.0, map_id: 1, room_id: 1)
# Transition.create(x_coordinate: 80.0, y_coordinate: 660.0, map_id: 1, room_id: 1)
# Transition.create(x_coordinate: 127.0, y_coordinate: 1259.7137, map_id: 1)
# Transition.create(x_coordinate: 123.490364, y_coordinate: 1068.0, map_id: 1)
# Transition.create(x_coordinate: 267.0, y_coordinate: 1064.0454, map_id: 1)
# # Transition.create(x_coordinate: 259.0, y_coordinate: 856.0, map_id: 1)
# Transition.create(x_coordinate: 219.0, y_coordinate: 675.19904, map_id: 1)
# Transition.create(x_coordinate: 267.0, y_coordinate: 640.0, map_id: 1)
# Transition.create(x_coordinate: 187.0, y_coordinate: 608.0, map_id: 1, room_id: 2)
# Transition.create(x_coordinate: 315.0, y_coordinate: 612.0, map_id: 1, room_id: 3)
# Transition.create(x_coordinate: 315.0, y_coordinate: 672.0, map_id: 1, room_id: 10)
# Transition.create(x_coordinate: 427.0, y_coordinate: 636.6154, map_id: 1)
# Transition.create(x_coordinate: 407.0, y_coordinate: 627.0, map_id: 1, room_id: 11)
# Transition.create(x_coordinate: 451.0, y_coordinate: 676.0, map_id: 1, room_id: 12)
# Transition.create(x_coordinate: 455.0, y_coordinate: 604.0, map_id: 1, room_id: 4)
# Transition.create(x_coordinate: 563.0, y_coordinate: 632.0, map_id: 1)
# Transition.create(x_coordinate: 535.0, y_coordinate: 672.0, map_id: 1, room_id: 13)
# Transition.create(x_coordinate: 591.0, y_coordinate: 672.0, map_id: 1, room_id: 14)
# Transition.create(x_coordinate: 647.0, y_coordinate: 632.0, map_id: 1)
# Transition.create(x_coordinate: 623.0, y_coordinate: 581.0, map_id: 1, room_id: 5)
# Transition.create(x_coordinate: 710.0, y_coordinate: 672.0, map_id: 1, room_id: 15)
# Transition.create(x_coordinate: 770.0, y_coordinate: 632.0, map_id: 1)
# Transition.create(x_coordinate: 750.01483, y_coordinate: 604.0, map_id: 1, room_id: 6)
# Transition.create(x_coordinate: 766.0, y_coordinate: 672.0, map_id: 1, room_id: 16)
# Transition.create(x_coordinate: 866.0, y_coordinate: 631.7191, map_id: 1)
# Transition.create(x_coordinate: 846.0, y_coordinate: 672.0, map_id: 1, room_id: 17)
# Transition.create(x_coordinate: 894.0, y_coordinate: 672.0, map_id: 1, room_id: 18)
# Transition.create(x_coordinate: 894.0, y_coordinate: 597.0, map_id: 1, room_id: 7)
# Transition.create(x_coordinate: 1002.0, y_coordinate: 630.9849, map_id: 1)
# Transition.create(x_coordinate: 986.0, y_coordinate: 668.0, map_id: 1, room_id: 19)
# Transition.create(x_coordinate: 1034.0, y_coordinate: 672.0, map_id: 1, room_id: 20)
# Transition.create(x_coordinate: 1034.0, y_coordinate: 600.97833, map_id: 1, room_id: 8)

# Beacons
# Beacon.create(id: 1, x_coordinate: 500.0, y_coordinate: 1000.0, map_id: 1, mac_address: '20:CD:39:B0:76:E9')
# Beacon.create(id: 2, x_coordinate: 520.0, y_coordinate: 1030.0, map_id: 1, mac_address: '20:CD:39:AE:FB:AA')

# Beacon.create(id: 1, x_coordinate: 14.835903, y_coordinate: 377.0, map_id: 1, mac_address: '20:CD:39:B0:76:E9')
# Beacon.create(id: 2, x_coordinate: 151.0, y_coordinate: 373.0, map_id: 1, mac_address: '20:CD:39:AE:FB:AA')

# Beacon.create(id: 1, x_coordinate: 14.835903, y_coordinate: 377.0, map_id: 1, mac_address: '20:CD:39:B0:64:48')
# Beacon.create(id: 2, x_coordinate: 151.0, y_coordinate: 373.0, map_id: 1, mac_address: '20:CD:39:B0:71:65')
# Beacon.create(id: 3, x_coordinate: 155.0, y_coordinate: 668.0, map_id: 1, mac_address: '20:CD:39:B1:15:AA')
# Beacon.create(id: 4, x_coordinate: 15.0, y_coordinate: 668.0, map_id: 1, mac_address: '20:CD:39:B1:14:14')
# Beacon.create(id: 5, x_coordinate: 14.292226, y_coordinate: 884.0, map_id: 1, mac_address: '20:CD:39:B0:74:77')
# Beacon.create(id: 6, x_coordinate: 159.0, y_coordinate: 888.0, map_id: 1, mac_address: '20:CD:39:B0:7C:38')

# Rooms
# Room.create(id: 1, name: 'R1', xmin: 7.0, ymin: 317.0, xmax: 159.06033, ymax: 924.0, map_id: 1)
# Room.create(id: 2, name: 'R2', xmin: 171.0, ymin: 321.0, xmax: 291.0, ymax: 612.0447, map_id: 1)
# Room.create(id: 3, name: 'R3', xmin: 295.0, ymin: 316.786, xmax: 422.9464, ymax: 612.0, map_id: 1)
# Room.create(id: 4, name: 'R4', xmin: 427.0, ymin: 317.0, xmax: 559.0, ymax: 612.0937, map_id: 1)
# Room.create(id: 5, name: 'R5', xmin: 599.0, ymin: 473.04718, xmax: 702.0, ymax: 592.98193, map_id: 1)
# Room.create(id: 6, name: 'R6', xmin: 734.0, ymin: 316.95132, xmax: 866.0, ymax: 612.09033, map_id: 1)
# Room.create(id: 7, name: 'R7', xmin: 870.0, ymin: 317.0, xmax: 1002.03937, ymax: 608.0, map_id: 1)
# Room.create(id: 8, name: 'R8', xmin: 1006.0, ymin: 316.97757, xmax: 1066.0, ymax: 607.86395, map_id: 1)
# Room.create(id: 9, name: 'R9', xmin: 7.0, ymin: 928.0, xmax: 99.0, ymax: 1204.1508, map_id: 1)
# Room.create(id: 10, name: 'R10', xmin: 291.0, ymin: 672.04175, xmax: 350.85028, ymax: 788.0, map_id: 1)
# Room.create(id: 11, name: 'R11', xmin: 363.0, ymin: 671.9557, xmax: 422.90656, ymax: 792.0, map_id: 1)
# Room.create(id: 12, name: 'R12', xmin: 427.0, ymin: 671.9703, xmax: 483.0, ymax: 791.3729, map_id: 1)
# Room.create(id: 13, name: 'R13', xmin: 499.0, ymin: 676.00745, xmax: 559.0, ymax: 788.0, map_id: 1)
# Room.create(id: 14, name: 'R14', xmin: 559.0, ymin: 672.0721, xmax: 623.0, ymax: 788.0, map_id: 1)
# Room.create(id: 15, name: 'R15', xmin: 666.0, ymin: 671.9379, xmax: 726.0, ymax: 791.91235, map_id: 1)
# Room.create(id: 16, name: 'R16', xmin: 730.0, ymin: 671.9594, xmax: 786.0, ymax: 791.7629, map_id: 1)
# Room.create(id: 17, name: 'R17', xmin: 806.0, ymin: 672.0, xmax: 862.0, ymax: 787.9613, map_id: 1)
# Room.create(id: 18, name: 'R18', xmin: 874.0, ymin: 676.03326, xmax: 925.8669, ymax: 788.0, map_id: 1)
# Room.create(id: 19, name: 'R19', xmin: 942.0, ymin: 671.9651, xmax: 1002.0, ymax: 792.0, map_id: 1)
# Room.create(id: 20, name: 'R20', xmin: 1005.9732, ymin: 672.0, xmax: 1062.0, ymax: 788.1389, map_id: 1)
