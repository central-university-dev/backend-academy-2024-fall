package service

import (
	desiredConfig "gitlab.tcsbank.ru/dbaas/cache-agent-gateway/internal/desired/domain"
)

//go:generate mockery --name DesiredConfigCache --structname MockDesiredConfigCache --filename mock_desired_config_cache_test.go --outpkg service_test --output .
type DesiredConfigCache interface {
	GetNode(nodeID string) (desiredConfig.Node, bool)
}
