// g++ -I/opt/homebrew/opt/gmp/include -L/opt/homebrew/opt/gmp/lib -lgmp modexp.cpp -o modexp
#include <gmp.h>

void mod_exp(mpz_t result, const mpz_t x, const mpz_t a, const mpz_t m) {
    if (mpz_cmp_ui(m, 1) == 0) {
        mpz_set_ui(result, 0);
        return;
    }

    mpz_t base, exp, mod_result;
    mpz_inits(base, exp, mod_result, NULL);

    mpz_set(base, x);
    mpz_set(exp, a);
    mpz_set_ui(result, 1);  // result = 1

    while (mpz_cmp_ui(exp, 0) > 0) {
        if (mpz_odd_p(exp)) {
            mpz_mul(result, result, base);
            mpz_mod(result, result, m);
        }

        mpz_mul(base, base, base);
        mpz_mod(base, base, m);

        mpz_fdiv_q_2exp(exp, exp, 1);  // exp >>= 1
    }

    mpz_clears(base, exp, mod_result, NULL);
}

int main() {
    mpz_t x, a, m, result;
    mpz_inits(x, a, m, result, NULL);

    mpz_set_str(x, "2", 10);
    mpz_set_str(a, "10203", 10);
    mpz_set_str(m, "101", 10);

    mod_exp(result, x, a, m);

    gmp_printf("Result: %Zd\n", result);

    mpz_clears(x, a, m, result, NULL);
    return 0;
}
