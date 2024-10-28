package hostRole_test

import (
	"context"
	"fmt"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"

	hostRole "backend-academy/Go/seminar9/examples/part3/hostrole"
)

func TestChecker_Check(t *testing.T) {
	t.Parallel()

	type TestCase struct {
		name          string
		clientBuilder func(t *testing.T) hostRole.PatroniClient
		expected      hostRole.HostRole
		err           error
	}

	testErr1 := fmt.Errorf("test error 1")

	testCases := []TestCase{
		{
			name: "ok primary",
			clientBuilder: func(t *testing.T) hostRole.PatroniClient {
				t.Helper()

				result := NewMockPatroniClient(t)
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLPrimary).
					Return(true, nil).
					Once()

				return result
			},
			expected: hostRole.Primary,
			err:      nil,
		},
		{
			name: "fail primary",
			clientBuilder: func(t *testing.T) hostRole.PatroniClient {
				t.Helper()

				result := NewMockPatroniClient(t)
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLPrimary).
					Return(false, testErr1).
					Once()

				return result
			},
			expected: hostRole.Unknown,
			err:      fmt.Errorf("failed to request patroni: test error 1"),
		},
		{
			name: "ok sync",
			clientBuilder: func(t *testing.T) hostRole.PatroniClient {
				t.Helper()

				result := NewMockPatroniClient(t)
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLPrimary).
					Return(false, nil).
					Once()
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLSync).
					Return(true, nil).
					Once()

				return result
			},
			expected: hostRole.SyncReplica,
			err:      nil,
		},
		{
			name: "fail sync",
			clientBuilder: func(t *testing.T) hostRole.PatroniClient {
				t.Helper()

				result := NewMockPatroniClient(t)
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLPrimary).
					Return(false, nil).
					Once()
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLSync).
					Return(false, testErr1).
					Once()

				return result
			},
			expected: hostRole.Unknown,
			err:      fmt.Errorf("failed to request patroni: test error 1"),
		},
		{
			name: "ok async",
			clientBuilder: func(t *testing.T) hostRole.PatroniClient {
				t.Helper()

				result := NewMockPatroniClient(t)
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLPrimary).
					Return(false, nil).
					Once()
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLSync).
					Return(false, nil).
					Once()
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLAsync).
					Return(true, nil).
					Once()

				return result
			},
			expected: hostRole.AsyncReplica,
			err:      nil,
		},
		{
			name: "fail async",
			clientBuilder: func(t *testing.T) hostRole.PatroniClient {
				t.Helper()

				result := NewMockPatroniClient(t)
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLPrimary).
					Return(false, nil).
					Once()
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLSync).
					Return(false, nil).
					Once()
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLAsync).
					Return(false, testErr1).
					Once()

				return result
			},
			expected: hostRole.Unknown,
			err:      fmt.Errorf("failed to request patroni: test error 1"),
		},
		{
			name: "all checks rejected",
			clientBuilder: func(t *testing.T) hostRole.PatroniClient {
				t.Helper()

				result := NewMockPatroniClient(t)
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLPrimary).
					Return(false, nil).
					Once()
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLSync).
					Return(false, nil).
					Once()
				result.
					On("YesNoCheck", mock.Anything, hostRole.SubURLAsync).
					Return(false, nil).
					Once()

				return result
			},
			expected: hostRole.Unknown,
			err:      fmt.Errorf("all patroni checks were rejected"),
		},
	}

	for _, tc := range testCases {
		tc := tc
		t.Run(tc.name, func(tt *testing.T) {
			tt.Parallel()

			checker := hostRole.NewChecker(tc.clientBuilder(tt))
			ctx := context.Background()
			actual, err := checker.Check(ctx)

			if tc.err != nil {
				assert.Equal(tt, hostRole.Unknown, actual)
				assert.EqualError(tt, err, tc.err.Error())

				return
			}

			assert.Equal(tt, tc.expected, actual)
			assert.NoError(tt, err)
		})
	}
}
