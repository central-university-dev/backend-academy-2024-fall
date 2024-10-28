package hostRole

import (
	"context"
)

//go:generate mockery --name PatroniClient --structname MockPatroniClient --filename mock_patroni_client_test.go --outpkg hostRole_test --output .
type PatroniClient interface {
	YesNoCheck(ctx context.Context, subURL string) (bool, error)
}
