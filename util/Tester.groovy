package util

class Tester {
	void test(object) {
		println "The object is a ${object.class.name}"
		println "${object.url} ${object.username}:${object.password}"
	}
}