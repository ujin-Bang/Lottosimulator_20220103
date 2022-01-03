package com.neppplus.lottosimulator_20220103

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    val mWinLottoNumArr = ArrayList<Int>()
    var mBonusNum = 0
    lateinit var mLottoNumTxtList : ArrayList<TextView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setValues()
        setupEvents()
    }

    fun setupEvents(){

        btnBuyLotto.setOnClickListener {

            // 로또 번호 6개 생성
            makeLottoNumbers()

//            보너스 번호 생성
            makeBonusNum()

        }

    }

    fun makeBonusNum() {

        // 써도 되는 숫자가 나올 때까지 무한 반복
        while (true) {

            val randomNum = (1..45).random()

            val isDuplOk = !mWinLottoNumArr.contains(randomNum)

            if(isDuplOk){
                mBonusNum = randomNum
                break

            }
        }
        // 보너스 번호 텍스트뷰에 반영
        txtBonusNum.text = mBonusNum.toString()
    }

    fun makeLottoNumbers(){
        // 기존에 번호가 있다면 전부 삭제.
        mWinLottoNumArr.clear()

        // 6개의 당첨 번호 => 반복 횟수 명확 =>for
        for (i in 0 until 6) {

            //랜덤 숫자 추출 -> (제대로 된 숫자라면)목록에 추가
            while (true) {

                val randomNum = (Math.random() * 45+1).toInt()
                // 중복 검사 : 당첨 목록에 내 숫자가 있는지?

                val isDuplOk = !mWinLottoNumArr.contains(randomNum)



                if(isDuplOk) {
                    //숫자를 당첨 목록에 추가.
                    mWinLottoNumArr.add(randomNum)
                    break;
                }

            }
        }
        // ArrayList의 sort 기능 활용
        mWinLottoNumArr.sort()

        // 당첨 번호 6개 확인

       for(i in 0 until 6) {
           // 텍스트뷰[i] = 당첨번호[i]
           mLottoNumTxtList[i].text = mWinLottoNumArr[i].toString()
       }
    }

    fun setValues(){

        mLottoNumTxtList = arrayListOf(txtLottoNum1,txtLottoNum2,txtLottoNum3,txtLottoNum4,txtLottoNum5,txtLottoNum6)


    }
}