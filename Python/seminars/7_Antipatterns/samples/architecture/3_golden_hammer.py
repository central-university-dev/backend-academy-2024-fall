from time import process_time


def bubble_sort(arr):
    n = len(arr)
    for i in range(n):
        for j in range(0, n - i - 1):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
    return arr


if __name__ == '__main__':
    # Использование пузырьковой сортировки везде

    data = [64, 34, 25, 12, 22, 11, 90]
    t1_start = process_time()
    _ = bubble_sort(data)
    print(f"Bubble sort small: {process_time() - t1_start}")

    data = [i for i in range(10**3)]
    t1_start = process_time()
    _ = bubble_sort(data)  # Неэффективно для больших массивов
    print(f"Bubble sort small: {process_time() - t1_start}")
