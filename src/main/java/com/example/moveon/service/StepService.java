package com.example.moveon.service;

import com.example.moveon.domain.StepRecord;
import com.example.moveon.domain.User;
import com.example.moveon.exception.AppException;
import com.example.moveon.exception.ErrorCode;
import com.example.moveon.repository.StepRecordRepository;
import com.example.moveon.repository.UserRepository;
import com.example.moveon.util.DateUtils;
import com.example.moveon.web.dto.UserDTO.UserStepInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StepService {
    private final UserRepository userRepository;
    private final StepRecordRepository stepRecordRepository;

    /*
     * 매일 자정에 user의 걸음수를 저장한다
     * @Param userStepInfo Dto (userId, userSteps, date)
     * */
    @Transactional
    public void postUserStep(UserStepInfo userStepInfo) {
        User user = userRepository.findById(userStepInfo.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        stepRecordRepository.save(
                StepRecord.builder()
                        .user(user)
                        .steps(userStepInfo.getSteps())
                        .date(userStepInfo.getDate())
                        .distance(userStepInfo.getDistance())
                        .HRate(userStepInfo.getHRate())
                        .run_time(userStepInfo.getRun_time())
                        .memo(userStepInfo.getMemo())
                        .build()
        );
    }

    /*
     * 1주일간의 user의 걸음수를 가져온다
     * @Param userId
     * @Param startDate 시작날짜
     * @Param endDate 종료 날짜 (시작날짜+7)
     * @return 7일간 걸음수 List
     * */
    public List<Integer> getUserStepWhileWeek(Long userId, Date startDate, Date endDate) {
        List<StepRecord> stepRecordList = stepRecordRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        Map<Date, Integer> stepsMap = stepRecordList.stream()
                .collect(Collectors.toMap(StepRecord::getDate, StepRecord::getSteps));

        List<Integer> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            Date currentDate = DateUtils.truncateTime(calendar.getTime());
            int steps = stepsMap.getOrDefault(currentDate, 0);
            result.add(steps);
            calendar.add(Calendar.DATE, 1);
        }
        return result;
    }
}
