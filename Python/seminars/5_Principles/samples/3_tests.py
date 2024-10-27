@fixture()
def task_manager():
    return Mock()


def test_add(task_manager):
    task_manager.add('task')
    assert 'task' in task_manager


def test_print(task_manager):
    assert task_manager.print()


def test_del_1(task_manager):
    task_manager.delete('task')
    assert 'task' not in task_manager

def test_del_2(task_manager):
    task_manager.delete('task')
    catch raises()


def test_add_to_list(todo_list):
    if not todo_list.full():
        todo_list.append(new_task)
    elif check_for_task_deadline(new_task) > check_for_task_deadline(current_task):
        pass
    else:
        todo_list.priority_remove(current_task)
        todo_list.append(new_task)

def test_delete_from_list(todo_list):
    if todo_list.empty():
        raise Exception(NothingToDeleteException)
    else if check_for_task_deadline(todo_list).equal() and not todo_list.empty():
        todo_list.delete_any()
    else:
        todo_list.priority_remove()


def add_task(fixture):
    task_list.add(task)
    last_task = task_list.tasks[:-1]

    if task_list.tasts_cnt >= task.list.size:
        assert task_list.tasks[:-1] == task
        assert TaskList.sorted(task_list)
    else:
        assert task_list[:-1] == task
        assert task_list[:-2] != last_task


import pytest

@pytest.fixture
def todo_list():
    return TodoList()

def test_add_task(todo_list):
    todo_list.add_task("Купить молоко")
    assert len(todo_list.get_tasks()) == 1
    assert todo_list.get_tasks()[0] == "Купить молоко"

def test_remove_task(todo_list):
    todo_list.add_task("Купить молоко")
    todo_list.add_task("Сделать зарядку")
    todo_list.remove_task("Купить молоко")

    assert len(todo_list.get_tasks()) == 1
    assert todo_list.get_tasks()[0] == "Сделать зарядку"

def test_view_tasks(todo_list):
    todo_list.add_task("Купить молоко")
    todo_list.add_task("Сделать зарядку")
    tasks = todo_list.get_tasks()

    assert tasks == ["Купить молоко", "Сделать зарядку"]