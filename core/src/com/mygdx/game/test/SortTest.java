package com.mygdx.game.test;

import java.util.Iterator;

import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;
import com.badlogic.gdx.utils.ShortArray;

public class SortTest {
	public static void main(String[] args) {
		int arrLength = 2;

		long startTime;
		long endTime;
		int[] src = new int[arrLength];
		for (int i = 0; i < arrLength; i++) {
			src[i] = new java.util.Random().nextInt();

		}
		startTime = System.nanoTime();
		//bubble(src);
		// quicksort(src, arrLength/2,src.length-arrLength/2);
		// mergeSort(src,10);
		endTime = System.nanoTime();
		System.out.println("time:" + (endTime - startTime));
		showArr(src);
	}

	public static void showArr(int[] src) {
		System.out.println();
		for (int i = 0; i < src.length; i++) {
			System.out.print(src[i]);
			System.out.print(",");
			if ((i - 1) % 10 == 0) {
				System.out.println();
			}
		}
	}

	public static void mergeSort(int[] src, int length) {
		if (src.length > length) {
			int[] arr1 = new int[src.length / 2];
			int[] arr2 = new int[src.length - (src.length / 2)];
			System.arraycopy(src, 0, arr1, 0, arr1.length);
			System.arraycopy(src, arr1.length, arr2, 0, arr2.length);
			mergeSort(arr1, length);
			mergeSort(arr2, length);

			int index = 0;
			int index1 = 0;
			int index2 = 0;
			while (true) {
				if (index1 == arr1.length) {
					System.arraycopy(arr2, index2, src, index, arr2.length - index2);
					break;
				}
				if (index2 == arr2.length) {
					System.arraycopy(arr1, index1, src, index, arr1.length - index1);
					break;
				}

				if (arr1[index1] > arr2[index2]) {
					src[index] = arr1[index1];
					index1++;
				} else {
					src[index] = arr2[index2];
					index2++;
				}
				index++;
			}
		} else {
			insertSort(src);
		}

	}
	/*
	 * public static void insertSort(int src[]){ for (int i = 1; i < src.length;
	 * i++) {
	 * 
	 * for (int j = 0; j < i; j++) { if (src[i]>src[j]) { int temp=src[i];
	 * System.arraycopy(src, j, src, j+1, src.length-(src.length-i+1)-j+1);
	 * src[j]=temp; break; } } } }
	 */

	static void quicksort(int n[], int left, int right) {
		int dp;
		if (left < right) {
			dp = partition(n, left, right);
			quicksort(n, left, dp - 1);
			quicksort(n, dp + 1, right);
		}
	}

	static int partition(int n[], int left, int right) {
		int pivot = n[left];
		while (left < right) {
			while (left < right && n[right] >= pivot)
				right--;
			if (left < right)
				n[left++] = n[right];
			while (left < right && n[left] <= pivot)
				left++;
			if (left < right)
				n[right--] = n[left];
		}
		n[left] = pivot;
		return left;
	}

	public static void insertSort(int[] array) {
		if (array == null || array.length < 2) {
			return;
		}

		for (int i = 1; i < array.length; i++) {
			int currentValue = array[i];
			int position = i;
			for (int j = i - 1; j >= 0; j--) {
				if (array[j] > currentValue) {
					array[j + 1] = array[j];
					position -= 1;
				} else {
					break;
				}
			}

			array[position] = currentValue;
		}
	}

	public static void bubbleSort(int[] values) {

		int temp;

		for (int i = 0; i < values.length; i++) {// 趟数

			for (int j = 0; j < values.length - i - 1; j++) {// 比较次数

				if (values[j] > values[j + 1]) {

					temp = values[j];

					values[j] = values[j + 1];

					values[j + 1] = temp;

				}

			}

		}

	}

	public static void bubble(int[] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data.length - 1 - i; j++) {
				if (data[j] > data[j + 1]) { // 如果后一个数小于前一个数交换
					int tmp = data[j];
					data[j] = data[j + 1];
					data[j + 1] = tmp;
				}
			}
		}
	}
	

}
