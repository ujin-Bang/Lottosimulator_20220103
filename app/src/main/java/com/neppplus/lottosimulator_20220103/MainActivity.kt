package com.neppplus.lottosimulator_20220103

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    val mWinLottoNumArr = ArrayList<Int>()
    var mBonusNum = 0

    //    내가 쓴 금액? 합산 변수
    var mUsedMoney = 0L //Long 타입( 긴 순자 표현) 명시

    //    당첨금액? 합산 변수
    var mEarnedMoney = 0L
    lateinit var mLottoNumTxtList: ArrayList<TextView>

    val mMyLottoNumArr = arrayListOf(5, 17, 26, 30, 36, 42)

    //    등수별 당첨 횟수 목록
    val mRankCountList = arrayListOf(0, 0, 0, 0, 0, 0)

//    지금 자동 구매 중인가?
    var isAutoNow = false

    //    할일을 관리하는 클래스
    lateinit var mHandler: Handler

    //    할일이 뭔지 작성 1) 로또 구매하기
    val buyLottoRunnable = object : Runnable {
        override fun run() {

            //        쓴 돈이 1천만원이 안된다면 -> 다시 로또 구매
            if (mUsedMoney <= 10000000) {
                makeLottoNumbers()
                makeBonusNum()
                checkLottoRank()

                mHandler.post(this)
            }
//        아니라면 반복 X
            else {
                Toast.makeText(this@MainActivity, "자동 구매를 중단합니다.", Toast.LENGTH_SHORT).show()
//            할일 추가 등록 X

            }


        }
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            setValues()
            setupEvents()
        }

        fun setupEvents() {

            btnAutoBuyLotto.setOnClickListener {

                if(!isAutoNow) {

//            핸들러에게 할일을 등록( 로또 한장 구매)
                    mHandler.post(buyLottoRunnable)

                    isAutoNow = true
                    btnAutoBuyLotto.text = "자동 구매 중단하기"
                }
                else {

//                    다음 구매 할일 제거
                    mHandler.removeCallbacks(buyLottoRunnable)

                    isAutoNow = false
                    btnAutoBuyLotto.text ="자동구매 재개"

                }



            }

            btnBuyLotto.setOnClickListener {

                // 로또 번호 6개 생성
                makeLottoNumbers()

//            보너스 번호 생성
                makeBonusNum()

                // 등수 판정
                checkLottoRank()
            }

        }

        fun checkLottoRank() {
//        1천원 사용으로 간주
            mUsedMoney += 1000


            // 내 숫자 6개가 -> 당첨번호 6개중 몇개나 맞췄는가?

            var correctCount = 0

            for (myNum in mMyLottoNumArr) {

                // 맞췄는가? -> myNum이 당첨번호에 들어있는가?
                if (mWinLottoNumArr.contains(myNum)) {
                    // 맞춘 갯수 증가
                    correctCount++

                }
            }
            Log.d("맞춘 갯수", "${correctCount}개 맞춤")
            //등수 판단 - when 활용

            when (correctCount) {
                6 -> {
                    Log.d("등수", "1등입니다")
                    mEarnedMoney += 5000000000

                    mRankCountList[0]++

                    txtRankCount1.text = "${mRankCountList[0]}회"
                }
                5 -> {
                    //보너스 번호 검사 -> 보너스 번호가 내 번호안에 있는가?
                    if (mMyLottoNumArr.contains(mBonusNum)) {
                        Log.d("등수", "2등")
                        mEarnedMoney += 50000000

                        mRankCountList[1]++

                        txtRankCount2.text = "${mRankCountList[1]}회"
                    } else {
                        Log.d("등수", "3등")
                        mEarnedMoney += 2000000

                        mRankCountList[2]++

                        txtRankCount3.text = "${mRankCountList[2]}회"
                    }
                }
                4 -> {
                    Log.d("등수", "4등")
                    mEarnedMoney += 50000

                    mRankCountList[3]++

                    txtRankCount4.text = "${mRankCountList[3]}회"
                }
                3 -> {
                    Log.d("등수", "5등")
                    mUsedMoney -= 5000
                    mRankCountList[4]++

                    txtRankCount5.text = "${mRankCountList[4]}회"
                }
                else -> {
                    Log.d("등수", "꽝 다음기회에..")

                    mRankCountList[5]++

                    txtRankCount6.text = "${mRankCountList[5]}회"
                }
            }
            txtUsedMoney.text = "${NumberFormat.getInstance(Locale.KOREA).format(mUsedMoney)}원"
            txtEarnMoney.text = "${NumberFormat.getInstance(Locale.KOREA).format(mEarnedMoney)}원"

        }

        fun makeBonusNum() {

            // 써도 되는 숫자가 나올 때까지 무한 반복
            while (true) {

                val randomNum = (1..45).random()

                val isDuplOk = !mWinLottoNumArr.contains(randomNum)

                if (isDuplOk) {
                    mBonusNum = randomNum
                    break

                }
            }
            // 보너스 번호 텍스트뷰에 반영
            txtBonusNum.text = mBonusNum.toString()
        }

        fun makeLottoNumbers() {
            // 기존에 번호가 있다면 전부 삭제.
            mWinLottoNumArr.clear()

            // 6개의 당첨 번호 => 반복 횟수 명확 =>for
            for (i in 0 until 6) {

                //랜덤 숫자 추출 -> (제대로 된 숫자라면)목록에 추가
                while (true) {

                    val randomNum = (Math.random() * 45 + 1).toInt()
                    // 중복 검사 : 당첨 목록에 내 숫자가 있는지?

                    val isDuplOk = !mWinLottoNumArr.contains(randomNum)



                    if (isDuplOk) {
                        //숫자를 당첨 목록에 추가.
                        mWinLottoNumArr.add(randomNum)
                        break;
                    }

                }
            }
            // ArrayList의 sort 기능 활용
            mWinLottoNumArr.sort()

            // 당첨 번호 6개 확인

            for (i in 0 until 6) {
                // 텍스트뷰[i] = 당첨번호[i]
                mLottoNumTxtList[i].text = mWinLottoNumArr[i].toString()
            }
        }

        fun setValues() {

            mHandler = Handler(Looper.getMainLooper())

            mLottoNumTxtList = arrayListOf(
                txtLottoNum1,
                txtLottoNum2,
                txtLottoNum3,
                txtLottoNum4,
                txtLottoNum5,
                txtLottoNum6
            )


        }
    }