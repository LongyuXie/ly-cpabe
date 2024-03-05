package org.example.linear

import org.ejml.data.DMatrixRMaj
import org.ejml.dense.row.CommonOps_DDRM
import org.ejml.dense.row.factory.DecompositionFactory_DDRM
import org.ejml.interfaces.decomposition.LUDecomposition_F64
import org.ejml.simple.SimpleMatrix
import org.junit.jupiter.api.Test


class TestMatrix {

    @Test
    fun test01() {
        val da = arrayOf(
            doubleArrayOf(1.0, 1.0, .0),
            doubleArrayOf(.0, -1.0, .0),
            doubleArrayOf(1.0, .0, 1.0),
            doubleArrayOf(.0, .0, -1.0),
        )
        val matrix = SimpleMatrix(da)

        val A = SimpleMatrix(da.sliceArray(1..2))
        val b = SimpleMatrix(doubleArrayOf(1.0, 0.0, 0.0))

        try {
            // solve方式并不准确
            val rec = A.transpose().solve(b)
            println(rec)
        } catch (e: Exception) {
            println("does not match")
        }
    }

    @Test
    fun testGauss() {
        val da = arrayOf(
            doubleArrayOf(1.0, 1.0, .0),
            doubleArrayOf(.0, -1.0, .0),
            doubleArrayOf(1.0, .0, 1.0),
            doubleArrayOf(.0, .0, -1.0),
        )

        val A = DMatrixRMaj(da.sliceArray(1..2))
        val b = DMatrixRMaj(doubleArrayOf(1.0, 0.0, 0.0))

        val luDecomp: LUDecomposition_F64<DMatrixRMaj> = DecompositionFactory_DDRM.lu(0)
        val decomposed = luDecomp.decompose(A)

        if (!decomposed) {
            println("Matrix A cannot be decomposed by LU decomposition.")
            return
        }

        // 解方程Ax = b
        val x = DMatrixRMaj(A.numRows, 1)


        // 打印解向量x
        println("Solution Vector x:")
        CommonOps_DDRM.transpose(x) // 为了打印方便，转置x使其成为行向量
        x.print()

    }
}