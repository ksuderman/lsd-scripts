package concurrent

interface AsyncService {
	Packet call(Packet packet)
}