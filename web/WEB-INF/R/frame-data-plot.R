
assign("t", plotType)
assign("dat", frame)
jpeg(imagePath,quality=90)
data(dat) 
attach(dat)
plot(x, y, col=unclass(x), type=t)
dev.off()