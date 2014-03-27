void func(Map map, String param1, String param2) {
	println "Parameters #1: ${param1}"
	println "Parameters #2: ${param2}"
	println "Map:"
	
	map.each { name,value ->
		println "${name}=${value}"
	}
}

func("p1", "p2", foo:'foo', bar:'bar')
