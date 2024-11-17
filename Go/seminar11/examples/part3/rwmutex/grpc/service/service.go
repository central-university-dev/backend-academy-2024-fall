package service

import (
	"context"

	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"

	v1beta1 "gitlab.tcsbank.ru/dbaas/cache-agent-gateway/internal/grpc/gateway/v1beta1"
)

type Service struct {
	v1beta1.UnimplementedGatewayServiceServer

	desiredConfigCache DesiredConfigCache
}

func NewService(
	desiredConfigCache DesiredConfigCache,
) *Service {
	return &Service{
		desiredConfigCache: desiredConfigCache,
	}
}

func (s *Service) GetNodeConfig(_ context.Context, req *v1beta1.GetNodeConfigRequest) (*v1beta1.GetNodeConfigResponse, error) {
	nodeID := req.GetNodeId()
	nodeCfg, ok := s.desiredConfigCache.GetNode(nodeID)
	if !ok {
		return nil, status.Errorf(codes.NotFound, "config for node %q not found", nodeID)
	}

	return NodeConfigToGrpcResponse(nodeCfg), nil
}
