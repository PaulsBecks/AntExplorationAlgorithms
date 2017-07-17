import matplotlib.pyplot as plt
import numpy as np
from mpl_toolkits.mplot3d import Axes3D
import pandas as pd

name = 'random_walk_output2'
df=pd.read_csv(name+'.csv')


fig = plt.figure()
ax1 = fig.add_subplot(111,projection='3d')

x= np.array(df['n'].data)
y=np.array(df['ants'].data)
z=np.array(df['time'].data)

ax1.scatter(x,y,z)

ax1.set_xlabel('Knoten')
ax1.set_ylabel('Ants')
ax1.set_zlabel('Zeitschritte')

plt.show() 
fig.savefig(name+'.png')
