package hostRole

type HostRole string

const (
	Primary      HostRole = "master"
	SyncReplica  HostRole = "replica_sync"
	AsyncReplica HostRole = "replica_async"
	Unknown      HostRole = "unknown"
)
