	.text
	.globl  main
main:
	move $fp, $sp
	sw $ra, -4($fp)
	subu $sp, $sp, 44
	li $a0 4
   	jal _halloc
   	move $s0 $v0
	li $a0 4
   	jal _halloc
   	move $s1 $v0
   	la $s2 Fac_Kums_ComputeFac
	sw $s2, 0($s1)
	sw $s1, 0($s0)
   	move $s3 $s0
   	li $s4 10
   	li $s5 0
	addu $s6, $s4, $s5
	lw $s7, 0($s3)
	lw $t0, 0($s7)
	sw $t0, 0($sp)
	sw $t1, 4($sp)
	sw $t2, 8($sp)
	sw $t3, 12($sp)
	sw $t4, 16($sp)
	sw $t5, 20($sp)
	sw $t6, 24($sp)
	sw $t7, 28($sp)
	sw $t8, 32($sp)
	sw $t9, 36($sp)
   	move $a0 $s3
   	move $a1 $s6
	jalr $t0
	lw $t0, 0($sp)
	lw $t1, 4($sp)
	lw $t2, 8($sp)
	lw $t3, 12($sp)
	lw $t4, 16($sp)
	lw $t5, 20($sp)
	lw $t6, 24($sp)
	lw $t7, 28($sp)
	lw $t8, 32($sp)
	lw $t9, 36($sp)
   	move $t1 $v0
	move $a0 $t1
   	jal _print
	addu $sp, $sp, 44
	lw $ra, -4($fp)
	j $ra

	.text
	.globl  Fac_Kums_ComputeFac
Fac_Kums_ComputeFac:
	sw $fp, -8($sp)
	move $fp, $sp
	sw $ra, -4($fp)
	subu $sp, $sp, 80
	sw $s0, 0($sp)
	sw $s1, 4($sp)
	sw $s2, 8($sp)
	sw $s3, 12($sp)
	sw $s4, 16($sp)
	sw $s5, 20($sp)
	sw $s6, 24($sp)
	sw $s7, 28($sp)
   	move $s0 $a0
   	move $s1 $a1
   	move $s2 $s1
   	li $s3 1
	sle $s4, $s2, $s3
	beqz $s4 L0_Fac_Kums_ComputeFac
   	move $s5 $s1
   	li $s6 1
	sne $s7, $s5, $s6
	beqz $s7 L0_Fac_Kums_ComputeFac
   	li $t0 1
	b L1_Fac_Kums_ComputeFac
L0_Fac_Kums_ComputeFac:	nop
   	li $t0 0
L1_Fac_Kums_ComputeFac:	nop
	beqz $t0 L2_Fac_Kums_ComputeFac
   	li $t1 1
   	li $t2 0
	addu $t3, $t1, $t2
   	move $t4 $t3
	b L3_Fac_Kums_ComputeFac
L2_Fac_Kums_ComputeFac:	nop
   	move $t5 $s1
   	move $t6 $s0
   	move $t7 $s1
   	li $t8 1
	subu $t9, $t7, $t8
	lw $s2, 0($t6)
	lw $s3, 0($s2)
	sw $t0, 32($sp)
	sw $t1, 36($sp)
	sw $t2, 40($sp)
	sw $t3, 44($sp)
	sw $t4, 48($sp)
	sw $t5, 52($sp)
	sw $t6, 56($sp)
	sw $t7, 60($sp)
	sw $t8, 64($sp)
	sw $t9, 68($sp)
   	move $a0 $t6
   	move $a1 $t9
	jalr $s3
	lw $t0, 32($sp)
	lw $t1, 36($sp)
	lw $t2, 40($sp)
	lw $t3, 44($sp)
	lw $t4, 48($sp)
	lw $t5, 52($sp)
	lw $t6, 56($sp)
	lw $t7, 60($sp)
	lw $t8, 64($sp)
	lw $t9, 68($sp)
   	move $s4 $v0
	mul $s5, $t5, $s4
   	move $t4 $s5
L3_Fac_Kums_ComputeFac:	nop
   	move $s6 $t4
   	move $v0 $s6
	lw $s0, 0($sp)
	lw $s1, 4($sp)
	lw $s2, 8($sp)
	lw $s3, 12($sp)
	lw $s4, 16($sp)
	lw $s5, 20($sp)
	lw $s6, 24($sp)
	lw $s7, 28($sp)
	addu $sp, $sp, 80
	lw $ra, -4($fp)
	lw $fp, -8($sp)
	j $ra

	.text
	.globl _halloc
_halloc:
	li $v0, 9
	syscall
	j $ra

	.text
	.globl _print
_print:
	li $v0, 1
	syscall
	la $a0, newl
	li $v0, 4
	syscall
	j $ra

	.data
	.align 0
newl:	.asciiz "\n"
	.data
	.align 0
str_er:	.asciiz "ERROR: abnormal termination\n"
