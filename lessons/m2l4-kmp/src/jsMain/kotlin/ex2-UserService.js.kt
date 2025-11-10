package com.github.watching1981.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class UserService {
    actual fun serve(user: User): String = "JS Service for User $user"
}
