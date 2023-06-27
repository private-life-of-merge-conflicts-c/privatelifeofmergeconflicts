

my_data <- Collaborator.IntegrationPerDay;

#1 How often do developers integrate code?

shapiro.test(Collaborator.IntegrationPerDay$TI.DAY);
ks.test(Collaborator.IntegrationPerDay$TI.DAY, 'pnorm')

#2 How frequently do integration scenarios result in conflicts?
shapiro.test(Collaborator.IntegrationPerDay$FI.TI);
ks.test(Collaborator.IntegrationPerDay$FI.TI, 'pnorm')

# create histogram for both datasets
hist(Collaborator.IntegrationPerDay$TI.DAY, col='steelblue', main='TI/DAY')
hist(Collaborator.IntegrationPerDay$FI.TI, col='steelblue', main='FI/TI')

# create Q-Q plot for both datasets
qqnorm(Collaborator.IntegrationPerDay$TI.DAY, main='TI/DAY')
qqline(Collaborator.IntegrationPerDay$TI.DAY)

qqnorm(Collaborator.IntegrationPerDay$FI.TI, main='FI/T')
qqline(Collaborator.IntegrationPerDay$FI.TI)

#3 Are these data correlated?

#Kendall rank correlation coefficient
test <- cor(my_data$TI.DAY, my_data$FI.TI, method = "kendall")
test
test <- cor.test(my_data$TI.DAY, my_data$FI.TI, method = "kendall")
test

# Spearman correlation
test <- cor(my_data$TI.DAY, my_data$FI.TI, method = "spearman")
test
test <- cor.test(my_data$TI.DAY, my_data$FI.TI, method = "spearman")
test



################################  Extreme Merge ################################  

#1 How often do developers integrate code?

shapiro.test(extremeMerge$TotalInteg.PofDays);
ks.test(extremeMerge$TotalInteg.PofDays, 'pnorm')


#2 How frequently do integration scenarios result in conflicts?

shapiro.test(extremeMerge$TotalFailedInteg.TotalInteg);
ks.test(extremeMerge$TotalFailedInteg.TotalInteg, 'pnorm')

#create histogram for both datasets
hist(extremeMerge$TotalInteg.PofDays, col='steelblue', main='TI/DAY')
hist(extremeMerge$TotalFailedInteg.TotalInteg, col='steelblue', main='FI/TI')


#3 Are these data correlated?

#Kendall rank correlation coefficient
test <- cor(extremeMerge$TotalInteg.PofDays, extremeMerge$TotalFailedInteg.TotalInteg, method = "kendall")
test
test <- cor.test(extremeMerge$TotalInteg.PofDays, extremeMerge$TotalFailedInteg.TotalInteg, method = "kendall")
test

# Spearman correlation
test <- cor(extremeMerge$TotalInteg.PofDays, extremeMerge$TotalFailedInteg.TotalInteg, method = "spearman")
test <- cor.test(extremeMerge$TotalInteg.PofDays, extremeMerge$TotalFailedInteg.TotalInteg, method = "spearman")
test



################################ Extreme HI ################################  

#1 How often do developers integrate code?

shapiro.test(extremeHI$TotalInteg.PofDays);
ks.test(extremeHI$TotalInteg.PofDays, 'pnorm')


#2 How frequently do integration scenarios result in conflicts?

shapiro.test(extremeHI$TotalFailedInteg.TotalInteg);
ks.test(extremeHI$TotalFailedInteg.TotalInteg, 'pnorm')

#create histogram for both datasets
hist(extremeHI$TotalInteg.PofDays, col='steelblue', main='TI/DAY')
hist(extremeHI$TotalFailedInteg.TotalInteg, col='steelblue', main='FI/TI')


#3 Are these data correlated?

#Kendall rank correlation coefficient
test <- cor(extremeHI$TotalInteg.PofDays, extremeHI$TotalFailedInteg.TotalInteg, method = "kendall")
test <- cor.test(extremeHI$TotalInteg.PofDays, extremeHI$TotalFailedInteg.TotalInteg, method = "kendall")
test

# Spearman correlation
test <- cor(extremeHI$TotalInteg.PofDays, extremeHI$TotalFailedInteg.TotalInteg, method = "spearman")
test <- cor.test(extremeHI$TotalInteg.PofDays, extremeHI$TotalFailedInteg.TotalInteg, method = "spearman")
test


######################################################################################################################
library(tidyverse)
library(tidyquant)
library(ggdist)
library(ggthemes)

