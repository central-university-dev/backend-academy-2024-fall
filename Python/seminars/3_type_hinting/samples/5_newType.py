from typing import NewType, Dict, Optional

UserId = NewType('UserId', int)
UserName = NewType('UserName', str)

user_ids: Dict[UserId, UserName] = {}


def get_user_name(user_id: UserId) -> Optional[UserName]:
    return user_ids.get(user_id)


if __name__ == '__main__':
    curr_user_id = UserId(123)

    print(type(curr_user_id))

    user_ids[curr_user_id] = UserName("Mr.Anderson")

    print(get_user_name(curr_user_id))
    print(type(get_user_name(curr_user_id)))

    print(curr_user_id + curr_user_id)
    print(type(curr_user_id + curr_user_id))