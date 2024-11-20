package repository

import (
	"maps"
	"sync"

	"gitlab.tcsbank.ru/dbaas/cache-agent-gateway/internal/desired/domain"
)

// Cache keeps desired configuration for all domain.Node`s, with NodeID as the key.
// Data kept at Cache can be partially updated and partially deleted.
type Cache struct {
	mu      sync.RWMutex
	configs map[string]domain.Node
}

func NewCache() *Cache {
	return &Cache{configs: make(map[string]domain.Node)}
}

// GetAllNodes returns copy of all node configs
func (c *Cache) GetAllNodes() map[string]domain.Node {
	c.mu.RLock()
	defer c.mu.RUnlock()

	return maps.Clone(c.configs)
}

// GetNode gets configuration for specific node.
func (c *Cache) GetNode(nodeID string) (domain.Node, bool) {
	c.mu.RLock()
	defer c.mu.RUnlock()

	node, ok := c.configs[nodeID]

	return node, ok
}

// DiscardNodes removes configuration for nodes specified by their ids.
func (c *Cache) DiscardNodes(nodeIDs ...string) {
	c.mu.Lock()
	defer c.mu.Unlock()

	for _, nodeID := range nodeIDs {
		delete(c.configs, nodeID)
	}
}

// PatchNodes updates configuration for nodes specified by their ids.
// New data win in case of collisions.
func (c *Cache) PatchNodes(patch map[string]domain.Node) {
	c.mu.Lock()
	defer c.mu.Unlock()

	for nodeID, nodeCfg := range patch {
		c.configs[nodeID] = nodeCfg
	}
}
