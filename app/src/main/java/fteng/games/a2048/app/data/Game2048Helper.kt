package games.game2048

/*
 * This function moves all the non-null elements to the beginning of the list (by removing nulls) and merges equal elements.
 * The parameter 'double' specifies the way how to merge equal elements:
 * it returns a new element that should be present in the result list instead of two merged elements.
 *
 * If the function double("a") returns "aa",
 * then the function moveAndMergeEqual transforms the input in the following way:
 *   a, a, b -> aa, b
 *   a, null -> a
 *   b, null, a, a -> b, aa
 *   a, a, null, a -> aa, a
 *   a, null, a, a -> aa, a
*/
fun <T : Any> List<T?>.moveAndMergeEqual(double: (T) -> T): List<T> {
    val mList = this.toMutableList()
    val nList = mList.filterNotNull().toMutableList()
    val replaceList = emptyList<Int>().toMutableList()
    nList.forEachIndexed { index, _ ->
        run {
            if (index < (nList.size - 1)) {
                if (nList[index].equals(nList[index + 1]) && (!replaceList.contains(index))) {
                    nList[index] = double(nList[index])
                    replaceList.add(index + 1)
                }
            }
        }
    }

    val ret = nList.filterIndexed { index, _ -> (!replaceList.contains(index)) }
    return ret
}


